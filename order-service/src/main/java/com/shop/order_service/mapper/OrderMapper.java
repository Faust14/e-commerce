package com.shop.order_service.mapper;

import com.shop.order_service.domain.Order;
import com.shop.order_service.dto.request.CreateOrderRequest;
import com.shop.order_service.dto.response.OrderResponse;
import com.shop.order_service.dto.response.ProductResponse;
import com.shop.order_service.dto.response.UserResponse;
import com.shop.order_service.feign.ProductClient;
import com.shop.order_service.feign.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderMapper {

    private final ProductClient productClient;
    private final UserClient userClient;

    public Order toOrder(CreateOrderRequest createOrderRequest) {
        return  Order.builder()
                .userId(createOrderRequest.userId())
                .orderDate(LocalDateTime.now())
                .productIds(createOrderRequest.productIds())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        UserResponse user = userClient.getUserById(order.getUserId());

        List<ProductResponse> products = order.getProductIds().stream()
                .map(productClient::getProductById)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), user, products, order.getOrderDate());
    }
}
