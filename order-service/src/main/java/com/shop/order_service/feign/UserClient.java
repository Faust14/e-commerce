package com.shop.order_service.feign;

import com.shop.common.feign.FeignClientInterceptorConfig;
import com.shop.order_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081", configuration = FeignClientInterceptorConfig.class)
public interface UserClient {

    @GetMapping("api/user/{id}")
    UserResponse getUserById(@PathVariable Long id);
}
