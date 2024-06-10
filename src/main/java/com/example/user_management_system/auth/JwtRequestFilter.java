package com.example.user_management_system.auth;

import com.example.user_management_system.exception.BadRequestException;
import com.example.user_management_system.exception.InternalServerException;
import com.example.user_management_system.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService tokenUtil;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            final String authorization = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authorization != null && !authorization.isEmpty() && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                Optional<AuthToken> authToken = authTokenService.getAuthToken(token);

                if (!authToken.isPresent()) {
                    throw new BadRequestException(HttpStatus.UNAUTHORIZED, "Invalid access token");
                }

                username = userService.findById(authToken.get().getUserId()).get().getUsername();
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                boolean validToken = tokenUtil.validate(token);

                if (validToken) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new BadRequestException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
                }
            }

            chain.doFilter(request, response);
        } catch (BadRequestException | InternalServerException e) {
            logger.error("error ", e);
            resolver.resolveException(request, response, null, e);
        }
    }
}
