package com.shop.order_service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderRequest(
        @NotNull
        Long userId,
        @NotNull
        List<Long> productIds,
        @NotNull
        LocalDateTime localDateTime
) {
}
