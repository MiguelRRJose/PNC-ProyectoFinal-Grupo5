package com.example.cursoapp.controller.identity;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.identity.user.UpdateUserRequest;
import com.example.cursoapp.service.identity.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

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

    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        return buildResponse(
                usuarioService.getAllUsers(),
                "Users successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(
                usuarioService.getUserById(id),
                "User successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-role")
    public ResponseEntity<GeneralResponse> getByRole(@RequestParam Long roleId) {
        return buildResponse(
                usuarioService.getUsersByRole(roleId),
                "Users successfully found.",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateUser(@PathVariable Long id,
                                                       @RequestBody @Valid UpdateUserRequest request) {
        return buildResponse(
                usuarioService.updateUser(id, request),
                "User successfully updated.",
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<GeneralResponse> deactivateUser(@PathVariable Long id) {
        return buildResponse(
                usuarioService.deactivateUser(id),
                "User successfully deactivated.",
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<GeneralResponse> activateUser(@PathVariable Long id) {
        return buildResponse(
                usuarioService.activateUser(id),
                "User successfully activated.",
                HttpStatus.OK
        );
    }
}
