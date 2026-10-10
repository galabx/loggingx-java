package com.loggingx.web.auth;

import com.loggingx.auth.model.AuthToken;
import com.loggingx.auth.service.AuthenticationService;
import com.loggingx.common.api.ApiResponse;
import com.loggingx.web.auth.dto.AuthTokenResponse;
import com.loggingx.web.auth.dto.LoginRequest;
import com.loggingx.web.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(toResponse(authenticationService.register(request.username(), request.password())));
    }

    @PostMapping("/login")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(toResponse(authenticationService.login(request.username(), request.password())));
    }

    private AuthTokenResponse toResponse(AuthToken token) {
        return new AuthTokenResponse(token.accessToken(), token.tokenType(), token.expiresInSeconds());
    }

}
