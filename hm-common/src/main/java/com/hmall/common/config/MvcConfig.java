package com.hmall.common.config;

import com.hmall.common.interceptors.UserInfoInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-08 17:34
 **/
@Configuration
@ConditionalOnClass(DispatcherServlet.class)
public class MvcConfig implements WebMvcConfigurer {
	@Override
	//InterceptorRegistry 拦截器的注册器,这个注册器可以添加多个拦截器,addpath不加也可以 默认拦截所有的路径
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserInfoInterceptor());
	}
}
