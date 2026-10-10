package com.loggingx.auth.security;

import com.loggingx.auth.service.JwtTokenService;
import com.loggingx.dao.account.UserAccount;
import com.loggingx.dao.account.UserAccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserAccountRepository userAccountRepository;

    public JwtAuthenticationFilter(
            JwtTokenService jwtTokenService,
            UserAccountRepository userAccountRepository
    ) {
        this.jwtTokenService = jwtTokenService;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = resolveBearerToken(request);
            if (token != null) {
                String username = jwtTokenService.parseUsername(token);
                if (username != null && !username.isBlank()) {
                    userAccountRepository.findByUsername(username)
                            .map(this::authenticationFor)
                            .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken authenticationFor(UserAccount account) {
        return new UsernamePasswordAuthenticationToken(
                account.username(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + account.role()))
        );
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = authorization.substring(7).trim();
            return token.isEmpty() ? null : token;
        }
        return null;
    }
}
