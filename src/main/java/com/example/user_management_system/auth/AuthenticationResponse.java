package com.example.user_management_system.auth;

import com.example.user_management_system.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;

    private UserDto user;
}
