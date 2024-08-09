package com.hmall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-09 10:33
 **/
@ConfigurationProperties(prefix = "hm.cart")
@Component
@Data
public class CartProperties {
	private Integer maxItems;
}
