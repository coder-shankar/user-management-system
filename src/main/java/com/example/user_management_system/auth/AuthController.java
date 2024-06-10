package com.example.user_management_system.auth;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    @PermitAll
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        return service.login(authenticationRequest);
    }


    @PostMapping("/token")
    public AuthenticationResponse refreshAccessToken(@Valid @RequestBody RefreshTokenRequest accessTokenRequest) {
        return service.refreshAccessToken(accessTokenRequest);
    }
}
