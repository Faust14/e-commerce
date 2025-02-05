package com.shop.order_service.controller;

import com.shop.order_service.dto.request.CreateOrderRequest;
import com.shop.order_service.dto.response.OrderResponse;
import com.shop.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        OrderResponse orderResponse = orderService.createOrder(createOrderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{orderId}/{userId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        orderService.deleteOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }
}
