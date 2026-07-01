package com.example.cursoapp.controller.commerce;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.commerce.purchase.CreatePurchaseRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.commerce.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
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
                .uri(uri).message(message).status(status.value()).time(Instant.now()).data(data).build());
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        return buildResponse(purchaseService.getAllPurchases(), "Purchases successfully found.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(purchaseService.getPurchaseById(id), "Purchase successfully found.", HttpStatus.OK);
    }

    @GetMapping("/by-user")
    public ResponseEntity<GeneralResponse> getByUser(@RequestParam Long userId) {
        return buildResponse(purchaseService.getPurchasesByUser(userId), "Purchases successfully found.", HttpStatus.OK);
    }

    @GetMapping("/by-course")
    public ResponseEntity<GeneralResponse> getByCourse(@RequestParam Long courseId) {
        return buildResponse(purchaseService.getPurchasesByCourse(courseId), "Purchases successfully found.", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createPurchase(@RequestBody @Valid CreatePurchaseRequest request) {
        return buildResponse(purchaseService.createPurchase(request, getCurrentUserId()), "Purchase successfully completed.", HttpStatus.CREATED);
    }
}