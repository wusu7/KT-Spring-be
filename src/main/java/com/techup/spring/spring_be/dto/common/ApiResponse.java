package com.techup.spring.spring_be.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, 200, message, data, LocalDateTime.now());
    }

    public static ApiResponse<Void> ok(String message) {
        return new ApiResponse<>(true, 200, message, null, LocalDateTime.now());
    }

    public static ApiResponse<Void> error(int status, String message) {
        return new ApiResponse<>(false, status, message, null, LocalDateTime.now());
    }
}
