package com.example.user_management_system.auth;

import com.example.user_management_system.exception.BadRequestException;
import com.example.user_management_system.exception.ResourceNotFoundException;
import com.example.user_management_system.user.User;
import com.example.user_management_system.user.UserDto;
import com.example.user_management_system.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private JwtTokenService jwtTokenUtil;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            authenticationManager.authenticate(authentication);
            final String jwtToken = jwtTokenUtil.generateToken();
            UserDto user = userService.getUserByUsername(authenticationRequest.getUsername());

            authTokenService.create(user.getId(), jwtToken);

            return new AuthenticationResponse(jwtToken, user);
        } catch (Exception e) {
            log.error("Error {}", e);
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "Invalid Credential");
        }
    }


    public AuthenticationResponse refreshAccessToken(RefreshTokenRequest accessTokenRequest) {
        AuthToken authToken = authTokenService.getAuthToken(accessTokenRequest.getToken())
                                              .orElseThrow(() -> new BadRequestException(HttpStatus.BAD_REQUEST, "Invalid token token received"));

        User user = userService.findById(authToken.getUserId())
                               .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        final String jwtToken = jwtTokenUtil.generateToken();
        authTokenService.create(user.getId(), jwtToken);
        authTokenService.deleteAuthTokenByJWTToken(accessTokenRequest.getToken());
        UserDto userDto = userService.toUserDto(user);

        return new AuthenticationResponse(jwtToken, userDto);
    }
}
