package com.shop.user_service.exception;

public class LoginException extends IllegalArgumentException{
    public LoginException(String message) {
        super(message);
    }
}
