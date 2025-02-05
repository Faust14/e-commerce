package com.shop.order_service.controller.advice;


import com.shop.order_service.controller.OrderController;
import com.shop.order_service.dto.response.ErrorResponse;
import com.shop.order_service.exception.NotFoundException;
import com.shop.order_service.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(assignableTypes = {
        OrderController.class,
})
@Slf4j
public class OrderExceptionAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException notFoundException) {
        log.warn("Not found: {}", notFoundException.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), notFoundException.getMessage());
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ErrorResponse> permissionDenied(PermissionException permissionException) {
        log.warn("Permission denied. You are not authorized to perform this operation.");
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), permissionException.getMessage());
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> defaultException(Throwable throwable) {
        log.warn("Internal server error: {}", throwable.getMessage());
        ErrorResponse apiError = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getMessage());
        return ResponseEntity.status(500).body(apiError);
    }
}
