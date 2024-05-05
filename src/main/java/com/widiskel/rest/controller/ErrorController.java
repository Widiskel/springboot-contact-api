package com.widiskel.rest.controller;


import com.widiskel.rest.model.ApiRes;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiRes<String>> constraintViolationException(ConstraintViolationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.<String>builder().rc("01").msg("Validation Error").errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiRes<String>> ResponseStatusException(ResponseStatusException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.<String>builder().rc("02").msg(exception.getReason()).errors(exception.getReason()).build());
    }
}
