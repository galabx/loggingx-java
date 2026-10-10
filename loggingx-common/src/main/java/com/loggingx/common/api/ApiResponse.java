package com.loggingx.common.api;

/**
 * Standard envelope for successful HTTP responses.
 *
 * @param success whether the operation completed successfully
 * @param data    response payload
 * @param message human-readable result message
 */
public record ApiResponse<T>(boolean success, T data, String message) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "OK");
    }
}
