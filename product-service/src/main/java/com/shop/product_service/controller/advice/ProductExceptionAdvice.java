package com.shop.product_service.controller.advice;

import com.shop.product_service.dto.response.ErrorResponse;
import com.shop.product_service.exception.NotFoundException;
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
public class ProductExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestBody(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.warn("Invalid http request: {}", methodArgumentNotValidException.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.getMessage());
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
