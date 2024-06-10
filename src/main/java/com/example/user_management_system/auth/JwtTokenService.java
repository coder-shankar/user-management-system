package com.example.user_management_system.auth;

import com.example.user_management_system.exception.BadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@Slf4j
public class JwtTokenService {

    @Value("${app.auth.token.secret}")
    private String secret;

    @Value("${app.auth.token.expiration.minutes}")
    private Long expiration;

    public String generateToken() {

        return Jwts.builder()
                   .setClaims(new HashMap<>())
                   .setExpiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(secret)
                                .parseClaimsJws(token)
                                .getBody();

            return claimResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "Token has expired.");
        }
    }

    public boolean validate(String token) {
        try {
            Date tokenExpiryDate = extractClaims(token, Claims::getExpiration);
            return tokenExpiryDate.after(new Date());
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
