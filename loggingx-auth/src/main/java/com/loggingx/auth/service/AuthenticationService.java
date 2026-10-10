package com.loggingx.auth.service;

import com.loggingx.auth.model.AuthToken;
import com.loggingx.common.exception.ConflictException;
import com.loggingx.common.exception.UnauthorizedException;
import com.loggingx.dao.account.UserAccount;
import com.loggingx.dao.account.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthenticationService {

    private static final String DEFAULT_ROLE = "USER";

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthToken register(String username, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);

        UserAccount account = new UserAccount(
                UUID.randomUUID(),
                normalizedUsername,
                passwordEncoder.encode(rawPassword),
                DEFAULT_ROLE,
                Instant.now()
        );
        if (!userAccountRepository.saveIfAbsent(account)) {
            throw new ConflictException("Username is already registered");
        }
        return issueToken(account);
    }

    public AuthToken login(String username, String rawPassword) {
        UserAccount account = userAccountRepository.findByUsername(normalizeUsername(username))
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        if (!passwordEncoder.matches(rawPassword, account.encodedPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }
        return issueToken(account);
    }

    private AuthToken issueToken(UserAccount account) {
        return new AuthToken(
                jwtTokenService.issue(account.username()),
                "Bearer",
                jwtTokenService.expiresInSeconds()
        );
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
