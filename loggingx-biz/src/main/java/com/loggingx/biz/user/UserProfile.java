package com.loggingx.biz.user;

import java.time.Instant;
import java.util.UUID;

public record UserProfile(UUID id, String username, String role, Instant createdAt) {
}
