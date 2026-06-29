package com.example.cursoapp.controller.auth;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.auth.LoginRequest;
import com.example.cursoapp.dto.auth.RegisterRequest;
import com.example.cursoapp.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(Instant.now())
                        .data(data)
                        .build()
                );
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(@RequestBody @Valid RegisterRequest request) {
        return buildResponse(
                authService.register(request),
                "User successfully registered.",
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@RequestBody @Valid LoginRequest request) {
        return buildResponse(
                authService.login(request),
                "Login successful.",
                HttpStatus.OK
        );
    }
}
