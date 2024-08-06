package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-06 15:57
 **/

public class DefaultFeignConfig {
	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
