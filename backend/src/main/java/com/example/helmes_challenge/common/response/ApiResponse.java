package com.example.helmes_challenge.common.response;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        String message,
        T data,
        Object errors
) {
}
