package com.loggingx.dao.account;

import java.time.Instant;
import java.util.UUID;

public record UserAccount(
        UUID id,
        String username,
        String encodedPassword,
        String role,
        Instant createdAt
) {
}
