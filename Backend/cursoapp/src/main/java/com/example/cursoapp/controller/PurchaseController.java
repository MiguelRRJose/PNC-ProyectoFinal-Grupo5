package com.example.cursoapp.controller;

import com.example.cursoapp.dto.request.CreatePurchaseRequest;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.dto.response.PurchaseResponse;
import com.example.cursoapp.service.impl.PurchaseServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseServiceImpl purchaseService;

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

    @PostMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> createPurchase(
            @Valid @RequestBody CreatePurchaseRequest request,
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {
        PurchaseResponse response = purchaseService.createPurchase(request, userId);
        return buildResponse(response, "Purchase created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> getPurchasesByUser(
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {
        List<PurchaseResponse> response = purchaseService.getPurchasesByUser(userId);
        return buildResponse(response, "Purchases retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<GeneralResponse> getPurchasesByCourse(
            @PathVariable Long courseId,
            HttpServletRequest httpRequest) {
        List<PurchaseResponse> response = purchaseService.getPurchasesByCourse(courseId);
        return buildResponse(response, "Purchases retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getPurchaseById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        PurchaseResponse response = purchaseService.getPurchaseById(id);
        return buildResponse(response, "Purchase found.", HttpStatus.OK, httpRequest);
    }
}