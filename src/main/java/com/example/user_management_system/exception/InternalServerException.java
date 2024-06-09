package com.example.user_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InternalServerException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private String message = "Internal Server Error";
}
