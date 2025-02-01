package com.shop.user_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtAuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public UserDetails validateTokenAndGetUserDetailsInternal(String token) {
        if (!jwtUtil.validateToken(token)) {
            return null;
        }

        String userEmail = jwtUtil.extractEmail(token);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            return userDetailsService.loadUserByUsername(userEmail);
        }

        return null;
    }

}
