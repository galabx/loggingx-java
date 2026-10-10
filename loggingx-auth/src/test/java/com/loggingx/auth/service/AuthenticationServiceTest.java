package com.loggingx.auth.service;

import com.loggingx.auth.model.AuthToken;
import com.loggingx.common.exception.UnauthorizedException;
import com.loggingx.dao.account.UserAccount;
import com.loggingx.dao.account.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @Test
    void registerNormalizesUsernameAndIssuesToken() {
        AuthenticationService service = new AuthenticationService(
                userAccountRepository, passwordEncoder, jwtTokenService
        );
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userAccountRepository.saveIfAbsent(any())).thenReturn(true);
        when(jwtTokenService.issue("alice")).thenReturn("access-token");
        when(jwtTokenService.expiresInSeconds()).thenReturn(7200L);

        AuthToken token = service.register(" Alice ", "password123");

        assertEquals("access-token", token.accessToken());
        assertEquals("Bearer", token.tokenType());
        assertEquals(7200L, token.expiresInSeconds());

        ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass(UserAccount.class);
        verify(userAccountRepository).saveIfAbsent(accountCaptor.capture());
        assertEquals("alice", accountCaptor.getValue().username());
        assertEquals("encoded-password", accountCaptor.getValue().encodedPassword());
    }

    @Test
    void loginRejectsIncorrectPassword() {
        AuthenticationService service = new AuthenticationService(
                userAccountRepository, passwordEncoder, jwtTokenService
        );
        UserAccount account = new UserAccount(null, "alice", "encoded-password", "USER", null);
        when(userAccountRepository.findByUsername("alice")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> service.login("ALICE", "wrong-password"));
    }
}
