package com.shop.user_service.domain;

import com.shop.user_service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Slf4j
public enum Role {
    USER ("USER"),
    ADMIN("ADMIN");

    private final String name;

    public static Role getRoleByAlias(String alias) {
        return switch (alias.toUpperCase()) {
            case "USER" -> USER;
            case "ADMIN" -> ADMIN;
            default -> {
                log.error("can't recognize provided role alias: {}", alias);
                throw new NotFoundException("Role not found");
            }
        };
    }
}
