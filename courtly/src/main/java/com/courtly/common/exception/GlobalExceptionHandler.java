package com.courtly.common.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.courtly.user.exception.UserAlreadyExistsException;
import java.util.HashMap;

import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidationErrors(
        MethodArgumentNotValidException exception
) {
    Map<String, String> fieldErrors = new HashMap<>();

    exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                    fieldErrors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    )
            );

    Map<String, Object> body = Map.of(
            "timestamp", Instant.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "message", "Validation failed",
            "fields", fieldErrors
    );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);
    }
}