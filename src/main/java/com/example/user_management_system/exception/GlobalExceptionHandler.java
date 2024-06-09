package com.example.user_management_system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestException(BadRequestException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setErrors(ex.getErrors());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Bad Request");
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<Error.ErrorDetail> collect = fieldErrors.stream()
                                                     .map(fieldError -> new Error.ErrorDetail(fieldError.getField(),
                                                             fieldError.getDefaultMessage()))
                                                     .collect(Collectors.toList());
        response.setErrors(collect);
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Internal server error", ex);
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Internal server error");
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
