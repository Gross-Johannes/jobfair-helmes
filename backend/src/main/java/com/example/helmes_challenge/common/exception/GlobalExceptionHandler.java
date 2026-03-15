package com.example.helmes_challenge.common.exception;

import com.example.helmes_challenge.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        HttpStatus status = ex.getStatus();

        ApiResponse<Object> response = ApiResponse.builder()
                .message(ex.getMessage())
                .data(null)
                .errors(null)
                .build();

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> response = ApiResponse.builder()
                .message(ex.getMessage())
                .data(null)
                .errors(null)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
