package com.courtly.shared.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.courtly.auth.exception.InvalidCredentialsException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

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
