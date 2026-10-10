package com.loggingx.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "loggingx.security.jwt")
public class JwtProperties {

    private String secret = "change-this-development-secret-before-production-2026";
    private Duration expiration = Duration.ofHours(2);

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getExpiration() {
        return expiration;
    }

    public void setExpiration(Duration expiration) {
        this.expiration = expiration;
    }
}
