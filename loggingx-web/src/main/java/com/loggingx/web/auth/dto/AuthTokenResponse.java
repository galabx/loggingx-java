package com.loggingx.web.auth.dto;

public record AuthTokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
