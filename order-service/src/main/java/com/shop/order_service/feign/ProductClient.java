package com.shop.order_service.feign;

import com.shop.common.feign.FeignClientInterceptorConfig;
import com.shop.order_service.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://localhost:8082", configuration = FeignClientInterceptorConfig.class)
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable Long id);

    @PutMapping("/api/products/{productId}/reduceQuantity")
    void reduceProductQuantity(@PathVariable("productId") Long productId, @RequestParam("quantity") int quantity);
}
