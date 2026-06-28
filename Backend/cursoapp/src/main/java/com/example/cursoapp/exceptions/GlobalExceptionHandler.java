package com.example.cursoapp.exceptions;

import com.example.cursoapp.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePokemonNotFound(ResourceNotFoundException exception) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors().stream().collect(
                        java.util.stream.Collectors.toMap(
                                org.springframework.validation.FieldError::getField,
                                fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value"
                        )
                );

        return new ResponseEntity<>(
                ApiErrorResponse.builder()
                        .timestamp(Instant.now())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .errors(errors)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRuleException(BusinessRuleException exception) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}