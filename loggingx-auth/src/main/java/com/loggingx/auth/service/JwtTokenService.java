package com.loggingx.auth.service;

import com.loggingx.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtTokenService(JwtProperties properties) {
        this.properties = properties;
        if (properties.getSecret() == null || properties.getSecret().getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("loggingx.security.jwt.secret must contain at least 32 UTF-8 bytes");
        }
        if (properties.getExpiration() == null || properties.getExpiration().isZero() || properties.getExpiration().isNegative()) {
            throw new IllegalStateException("loggingx.security.jwt.expiration must be greater than zero");
        }
        this.signingKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String issue(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getExpiration());
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public String parseUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException exception) {
            return null;
        }
    }

    public long expiresInSeconds() {
        return properties.getExpiration().toSeconds();
    }
}
