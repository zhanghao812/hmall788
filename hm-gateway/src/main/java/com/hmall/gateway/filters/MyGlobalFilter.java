package com.hmall.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-08 09:19
 **/
@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {
//大多数排序都是利用order实现的
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO 模拟登录校验逻辑
		//获取登录凭证
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		System.out.println("headers="+headers);
		//放行
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
