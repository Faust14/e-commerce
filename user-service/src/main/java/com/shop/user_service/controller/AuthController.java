package com.shop.user_service.controller;

import com.shop.user_service.dto.request.CreateUserRequest;
import com.shop.user_service.dto.request.LoginRequest;
import com.shop.user_service.dto.response.LoginResponse;
import com.shop.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.doLogin(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> doRegister(@RequestBody CreateUserRequest createUserRequest) {
        authService.doRegister(createUserRequest);
        return ResponseEntity.status(201).build();
    }
}
