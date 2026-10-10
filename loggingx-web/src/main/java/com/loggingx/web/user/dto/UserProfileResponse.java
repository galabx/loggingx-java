package com.loggingx.web.user.dto;

import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(UUID id, String username, String role, Instant createdAt) {
}
