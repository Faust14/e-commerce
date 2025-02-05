package com.shop.user_service.dto.response;

import com.shop.user_service.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    String jtwToken;
    User user;
}
