package com.shop.order_service.unit;

import com.shop.order_service.domain.Order;
import com.shop.order_service.dto.request.CreateOrderRequest;
import com.shop.order_service.dto.response.CategoryResponse;
import com.shop.order_service.dto.response.OrderResponse;
import com.shop.order_service.dto.response.ProductResponse;
import com.shop.order_service.dto.response.UserResponse;
import com.shop.order_service.exception.NotFoundException;
import com.shop.order_service.exception.PermissionException;
import com.shop.order_service.feign.ProductClient;
import com.shop.order_service.feign.UserClient;
import com.shop.order_service.mapper.OrderMapper;
import com.shop.order_service.repository.OrderRepository;
import com.shop.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private UserClient userClient;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private UserResponse userResponse;
    private List<ProductResponse> products;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(1L, "John", "Doe", "johndoe", "john@example.com", "USER");

        CategoryResponse category = new CategoryResponse(1L, "Electronics");

        products = Arrays.asList(
                new ProductResponse(1L, "Product A", "High-quality product", category, LocalDateTime.now(), 99.99, 10),
                new ProductResponse(2L, "Product B", "Another great product", category, LocalDateTime.now(), 49.99, 5)
        );

        order = new Order();
        order.setId(1L);
        order.setUserId(userResponse.id());
        order.setProductIds(Arrays.asList(1L, 2L));
        order.setOrderDate(LocalDateTime.now());

        createOrderRequest = new CreateOrderRequest(1L, Arrays.asList(1L, 2L));
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toOrderResponse(order)).thenReturn(new OrderResponse(order.getId(), userResponse, products, order.getOrderDate()));

        List<OrderResponse> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).id());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrdersByUserId_ShouldReturnOrdersForUser() {
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));
        when(orderMapper.toOrderResponse(order)).thenReturn(new OrderResponse(order.getId(), userResponse, products, order.getOrderDate()));

        List<OrderResponse> result = orderService.getOrdersByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).id());
        verify(orderRepository, times(1)).findByUserId(1L);
    }

    @Test
    void createOrder_ShouldCreateAndReturnOrderResponse() {
        when(userClient.getUserById(1L)).thenReturn(userResponse);
        when(productClient.getProductById(1L)).thenReturn(products.get(0));
        when(productClient.getProductById(2L)).thenReturn(products.get(1));
        when(orderMapper.toOrder(createOrderRequest)).thenReturn(order);
        when(orderRepository.saveAndFlush(order)).thenReturn(order);

        OrderResponse response = orderService.createOrder(createOrderRequest);

        assertNotNull(response);
        assertEquals(order.getId(), response.id());
        verify(productClient, times(2)).reduceProductQuantity(anyLong(), eq(1));
    }

    @Test
    void createOrder_ShouldThrowException_WhenProductOutOfStock() {
        when(userClient.getUserById(1L)).thenReturn(userResponse);
        when(productClient.getProductById(1L)).thenReturn(new ProductResponse(1L, "Product A", "High-quality product",
                new CategoryResponse(1L, "Electronics"), LocalDateTime.now(), 99.99, 0));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            orderService.createOrder(createOrderRequest);
        });

        assertEquals("Product Product A is out of stock.", thrown.getMessage());
    }

    @Test
    void updateOrder_ShouldUpdateAndReturnOrderResponse() {
        when(userClient.getUserById(1L)).thenReturn(userResponse);
        when(productClient.getProductById(1L)).thenReturn(products.get(0));
        when(productClient.getProductById(2L)).thenReturn(products.get(1));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.updateOrder(order);

        assertNotNull(response);
        assertEquals(order.getId(), response.id());
        verify(orderRepository, times(1)).saveAndFlush(order);
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenUserIsAdmin() {
        UserResponse adminUser = new UserResponse(1L, "Admin", "User", "admin", "admin@example.com", "ADMIN");
        when(userClient.getUserById(1L)).thenReturn(adminUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L, 1L);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void deleteOrder_ShouldThrowPermissionException_WhenUserIsNotAdminOrOwner() {
        UserResponse anotherUser = new UserResponse(2L, "Jane", "Smith", "janesmith", "jane@example.com", "USER");
        when(userClient.getUserById(2L)).thenReturn(anotherUser);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        PermissionException thrown = assertThrows(PermissionException.class, () -> {
            orderService.deleteOrder(1L, 2L);
        });

        assertEquals("Permission denied. You are not authorized to perform this operation.", thrown.getMessage());
    }

    @Test
    void deleteOrder_ShouldThrowNotFoundException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            orderService.deleteOrder(1L, 1L);
        });

        assertEquals("Order not found", thrown.getMessage());
    }
}
