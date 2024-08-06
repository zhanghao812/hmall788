package com.hmall.api.client;

import com.hmall.api.dto.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-06 09:06
 **/
@FeignClient("item-service")
public interface ItemClient {
	@GetMapping("/items")
	List<ItemDTO> queryItemByIds(@RequestParam("id") Collection<Long> ids);
}
