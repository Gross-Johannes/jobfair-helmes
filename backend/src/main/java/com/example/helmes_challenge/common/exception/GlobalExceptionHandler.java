package com.example.helmes_challenge.common.exception;

import com.example.helmes_challenge.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request body";

        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof DateTimeParseException) {
            message = "Invalid date format. Please use a valid date";
        }

        ApiResponse<Object> response = ApiResponse.builder()
                .message(message)
                .data(null)
                .errors(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
