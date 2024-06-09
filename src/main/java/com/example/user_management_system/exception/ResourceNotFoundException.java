package com.example.user_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final String message;
}
