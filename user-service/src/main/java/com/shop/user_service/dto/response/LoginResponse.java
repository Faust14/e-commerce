package com.shop.user_service.dto.response;

import com.shop.user_service.domain.User;

public record LoginResponse(
        String jtwToken,
        User user
) {
}
