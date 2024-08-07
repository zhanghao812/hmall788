package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-07 09:05
 **/
@FeignClient(name = "user-service")
public interface UserClient {
	@PutMapping("/users/money/deduct")
	void deductMoney(@RequestParam("pw") String pw, @RequestParam("amount") Integer amount);
}
