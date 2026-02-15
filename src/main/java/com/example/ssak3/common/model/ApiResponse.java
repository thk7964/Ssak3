package com.example.ssak3.common.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse {

    private final boolean success;
    private final String message;
    private final Object data;
    private final LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiResponse success(String message, Object data) {

        return new ApiResponse(true, message, data);
    }

    public static ApiResponse error(String message) {

        return new ApiResponse(false, message, null);
    }
}
