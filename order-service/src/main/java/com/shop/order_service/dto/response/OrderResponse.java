package com.shop.order_service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse (
        Long id,
        UserResponse userResponse,
        List<ProductResponse> products,
        LocalDateTime localDateTime
) {
}
