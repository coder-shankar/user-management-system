package com.example.user_management_system.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthTokenService {
    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Value("${app.auth.token.expiration.minutes}")
    protected Long expiration;

    public String create(Long userId, String jwtToken) {
        log.info("Creating auth token for userId: {}", userId);
        AuthToken authToken = new AuthToken();
        authToken.setUserId(userId);
        authToken.setJwtToken(jwtToken);

        return authTokenRepository.save(authToken).getJwtToken();
    }

    public Optional<AuthToken> getAuthToken(String jwtToken) {
        return authTokenRepository.findByJwtToken(jwtToken);
    }

    public void deleteAuthTokenByJWTToken(String jwtToken) {
        Optional<AuthToken> authToken = authTokenRepository.findByJwtToken(jwtToken);
        authToken.ifPresent(authTokenRepository::delete);
    }
}
