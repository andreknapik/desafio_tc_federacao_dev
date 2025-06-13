
package com.example.federacao_develop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(
                new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex) {
        return new ResponseEntity<>(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleOthers(Exception ex) {
        return new ResponseEntity<>(
                new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor", LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}