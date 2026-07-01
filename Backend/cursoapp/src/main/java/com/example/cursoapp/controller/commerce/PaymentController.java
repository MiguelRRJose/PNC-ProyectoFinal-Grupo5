package com.example.cursoapp.controller.commerce;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.commerce.payment.PaymentRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.commerce.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UsuarioRepository usuarioRepository;

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status).body(GeneralResponse.builder()
                .uri(uri)
                .message(message)
                .status(status.value())
                .time(Instant.now())
                .data(data)
                .build());
    }

    @PostMapping("/checkout")
    public ResponseEntity<GeneralResponse> checkout(@RequestBody @Valid PaymentRequest request) {
        return buildResponse(
                paymentService.processPayment(request, getCurrentUserId()),
                "Payment processed successfully.",
                HttpStatus.OK
        );
    }
}