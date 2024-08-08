package com.hmall.gateway.filters;

import cn.hutool.core.collection.CollUtil;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.common.utils.CollUtils;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-08 11:25
 **/
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

	private final JwtTool jwtTool;

	private final AuthProperties authProperties;


	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		//1.获取request对象
		ServerHttpRequest request = exchange.getRequest();
		//2.判断是否需要做登录拦截
		if(isExclude(request.getPath().toString())){
			//放行
			return chain.filter(exchange);
		}
		//3.获取token
		String token =null;
		List<String> headers = request.getHeaders().get("authorization");
		if(CollUtils.isEmpty(headers)){
			//headers!=null &&!headers.isEmpty()
			token = headers.get(0);
		}
		//4.校验并解析token
		Long userId = null;
		try {
			userId = jwtTool.parseToken(token);
		} catch (UnauthorizedException e) {
			// 拦截,设置响应状态码401
			ServerHttpResponse response = exchange.getResponse();
//			response.setRawStatusCode(401);
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return response.setComplete();
		}
		//TODO 5.传递用户信息
		System.out.println("userId = " + userId);
		//6.放行
		return chain.filter(exchange);
	}

	private boolean isExclude(String path) {
		// /search/** 特殊的语法 不能用equal,这种特殊路径要用antPathMatcher
		for (String pathPattern : authProperties.getExcludePaths()) {
			if(antPathMatcher.match(pathPattern, path)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
