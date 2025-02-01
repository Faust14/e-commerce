package com.shop.product_service.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
        @NotNull String name
) {
}
