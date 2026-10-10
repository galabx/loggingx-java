package com.loggingx.auth.model;

public record AuthToken(String accessToken, String tokenType, long expiresInSeconds) {
}
