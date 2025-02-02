package com.shop.order_service.service;

import com.shop.order_service.domain.Order;
import com.shop.order_service.dto.request.CreateOrderRequest;
import com.shop.order_service.dto.response.OrderResponse;
import com.shop.order_service.dto.response.ProductResponse;
import com.shop.order_service.dto.response.UserResponse;
import com.shop.order_service.exception.NotFoundException;
import com.shop.order_service.feign.ProductClient;
import com.shop.order_service.feign.UserClient;
import com.shop.order_service.mapper.OrderMapper;
import com.shop.order_service.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService{

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductClient productClient, UserClient userClient, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
        this.orderMapper = orderMapper;
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {

        var user = userClient.getUserById(createOrderRequest.userId());
        List<ProductResponse> products = createOrderRequest.productIds().stream()
                .map(productClient::getProductById)
                .toList();
        Order order = orderMapper.toOrder(createOrderRequest);

        orderRepository.saveAndFlush(order);

        return new OrderResponse(order.getId(),user, products, order.getOrderDate());
    }

    @Transactional
    public OrderResponse updateOrder(Order order) {

        var user = userClient.getUserById(order.getUserId());
        List<ProductResponse> products = order.getProductIds().stream()
                .map(productClient::getProductById)
                .toList();

        Order existingOrder = orderRepository.findById(order.getId()).orElseThrow(() -> new NotFoundException("Category not found"));
        existingOrder.setProductIds(order.getProductIds());

        boolean isAdmin = user.role().equalsIgnoreCase("ADMIN");

        if (!isAdmin && !existingOrder.getUserId().equals(user.id())) {
            return null;
        }

        orderRepository.saveAndFlush(existingOrder);

        return new OrderResponse(order.getId(), user, products, order.getOrderDate());
    }

    @Transactional
    public void deleteOrder(Long orderId, Long userId) {

        var user = userClient.getUserById(userId);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        boolean isAdmin = user.role().equalsIgnoreCase("ADMIN");

        if (!isAdmin && !existingOrder.getUserId().equals(user.id())) {
            return;
        }

        orderRepository.delete(existingOrder);
    }
}
