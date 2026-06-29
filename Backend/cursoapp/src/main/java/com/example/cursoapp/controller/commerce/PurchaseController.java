package com.example.cursoapp.controller.commerce;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.commerce.purchase.CreatePurchaseRequest;
import com.example.cursoapp.service.commerce.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

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

    // Solo ADMIN debería ver todas las compras
    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        return buildResponse(
                purchaseService.getAllPurchases(),
                "Purchases successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(
                purchaseService.getPurchaseById(id),
                "Purchase successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-user")
    public ResponseEntity<GeneralResponse> getByUser(@RequestParam Long userId) {
        return buildResponse(
                purchaseService.getPurchasesByUser(userId),
                "Purchases successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-course")
    public ResponseEntity<GeneralResponse> getByCourse(@RequestParam Long courseId) {
        return buildResponse(
                purchaseService.getPurchasesByCourse(courseId),
                "Purchases successfully found.",
                HttpStatus.OK
        );
    }

    // Las compras no se borran ni modifican — solo se crean
    @PostMapping
    public ResponseEntity<GeneralResponse> createPurchase(@RequestBody @Valid CreatePurchaseRequest request) {
        Long userId = null; // TODO: obtener del contexto JWT
        return buildResponse(
                purchaseService.createPurchase(request, userId),
                "Purchase successfully completed.",
                HttpStatus.CREATED
        );
    }
}
