package com.example.cursoapp.controller;

import com.example.cursoapp.dto.request.RegisterRequest;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.dto.response.UsuarioResponse;
import com.example.cursoapp.service.impl.UsuarioServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                GeneralResponse.builder()
                        .data(data)
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        UsuarioResponse response = usuarioService.registerUser(request);
        return buildResponse(response, "User registered successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllUsers(HttpServletRequest httpRequest) {
        List<UsuarioResponse> response = usuarioService.getAllUsers();
        return buildResponse(response, "Users retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getUserById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        UsuarioResponse response = usuarioService.getUserById(id);
        return buildResponse(response, "User found.", HttpStatus.OK, httpRequest);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GeneralResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive,
            HttpServletRequest httpRequest) {
        UsuarioResponse response = usuarioService.updateUserStatus(id, isActive);
        return buildResponse(response, "User status updated successfully.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteUser(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        usuarioService.deleteUser(id);
        return buildResponse(null, "User deleted successfully.", HttpStatus.OK, httpRequest);
    }
}