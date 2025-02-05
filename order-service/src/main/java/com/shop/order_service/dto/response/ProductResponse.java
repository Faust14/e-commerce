package com.shop.order_service.dto.response;

import java.time.LocalDateTime;

public record ProductResponse (
        Long id,
        String name,
        String description,
        CategoryResponse category,
        LocalDateTime localDateTime,
        Double price,
        int quantity
) {
}
