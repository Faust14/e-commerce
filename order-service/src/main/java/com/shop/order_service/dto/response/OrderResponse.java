package com.shop.order_service.dto.response;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse (
        @NotNull
        Long id,
        @NotNull
        UserResponse userResponse,
        @NotNull
        List<ProductResponse> products,
        @NotNull
        LocalDateTime localDateTime
) {
}
