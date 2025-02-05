package com.shop.order_service.exception;

public class PermissionException extends IllegalAccessError {
    public PermissionException(String message) {
        super(message);
    }
}
