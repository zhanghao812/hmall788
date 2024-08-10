package com.hmall.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.hmall.common.utils.CollUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-09 15:00
 **/

@Slf4j
@Component // DynamicRouteLoader会初始化
@RequiredArgsConstructor
//动态路由的加载器
public class DynamicRouteLoader {

	private final RouteDefinitionWriter writer;
	private final NacosConfigManager nacosConfigManager; // DynamicRouteLoader会初始化 ，就会注入ConfigManager 拿到configservice

	// 路由配置文件的id和分组
	private final String dataId = "gateway-routes.json";
	private final String group = "DEFAULT_GROUP"; 	// 保存更新过的路由id
	private final Set<String> routeIds = new HashSet<>();

	@PostConstruct //在bean初始化之后执行 ，初始化监听器
	public void initRouteConfigListener() throws NacosException {
		// 1.项目启动，先拉去一次配置 并且添加配置监听器 注册监听器并首次拉取配置
		//configInfo 所有的路由表信息 转换成RouteDefinition，配置文件字符串形式 解析yaml不会 解析不方便
		String configInfo = nacosConfigManager.getConfigService()
				.getConfigAndSignListener(dataId, group, 5000, new Listener() {
					//既拉去又添加监听器
					@Override
					public Executor getExecutor() {
						return null;
					}

					@Override
					public void receiveConfigInfo(String configInfo) {
						//2.监听到配置变更，需要去更新路由表
						updateConfigInfo(configInfo);
					}
				});
		// 3.第一次读取到配置，也需要更新到路由表
		updateConfigInfo(configInfo);

	}
	//路由表的更新
	private void updateConfigInfo(String configInfo) {
		log.debug("监听到路由配置变更，{}", configInfo);
		//1. 解析配置文件，转为RouteDefinition
		// 1.反序列化
		List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
		// 2.更新前先清空旧路由,更新路由表
		// 2.1.清除旧路由
		for (String routeId : routeIds) {
			writer.delete(Mono.just(routeId)).subscribe();
		}
		routeIds.clear(); //清空路由id表
		// 2.2.判断是否有新的路由要更新
		if (CollUtils.isEmpty(routeDefinitions)) {
			// 无新路由配置，直接结束
			return;
		}
		// 3.更新路由 Mono springboot 提供的一个响应式编程的容器 subscribe订阅 订阅容器里面的消息
		routeDefinitions.forEach(routeDefinition -> {
			// 3.1.更新路由表
			writer.save(Mono.just(routeDefinition)).subscribe();
			// 3.2.记录路由id，便于下一次更新时删除
			routeIds.add(routeDefinition.getId());
		});
	}
}

