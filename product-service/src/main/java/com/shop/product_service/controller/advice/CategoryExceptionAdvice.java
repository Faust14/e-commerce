package com.shop.product_service.controller.advice;

import com.shop.product_service.dto.response.ErrorResponse;
import com.shop.product_service.exception.AlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CategoryExceptionAdvice {

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponse> invalidRequestBody(AlreadyExistException alreadyExistException) {
        log.warn("Category exist: {}", alreadyExistException.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.CONFLICT.name(), HttpStatus.CONFLICT.value(), alreadyExistException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}
