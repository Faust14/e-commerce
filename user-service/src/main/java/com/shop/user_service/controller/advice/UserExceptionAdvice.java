package com.shop.user_service.controller.advice;

import com.shop.user_service.controller.AuthController;
import com.shop.user_service.controller.UserController;
import com.shop.user_service.dto.response.ErrorResponse;
import com.shop.user_service.exception.LoginException;
import com.shop.user_service.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(assignableTypes = {
        AuthController.class,
        UserController.class
})
@Slf4j
public class UserExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestBody(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.warn("Invalid http request: {}", methodArgumentNotValidException.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.getMessage());
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> invalidLogin(LoginException loginException) {
        log.warn("Error login: {}", loginException.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), loginException.getMessage());
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException notFoundException) {
        log.warn("Not found: {}", notFoundException.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), notFoundException.getMessage());
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> defaultException(Throwable throwable) {
        log.warn("Internal server error: {}", throwable.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getMessage());
        return ResponseEntity.status(500).body(apiError);
    }
}
