package com.example.cursoapp.controller;

import com.example.cursoapp.dto.content.lection.CreateLectionRequest;
import com.example.cursoapp.dto.content.lection.UpdateLectionRequest;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.dto.content.lection.LectionResponse;
import com.example.cursoapp.service.impl.LectionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lections")
@RequiredArgsConstructor
public class LectionController {

    private final LectionServiceImpl lectionService;

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

    @PostMapping
    public ResponseEntity<GeneralResponse> createLection(
            @Valid @RequestBody CreateLectionRequest request,
            HttpServletRequest httpRequest) {
        LectionResponse response = lectionService.createLection(request);
        return buildResponse(response, "Lection created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<GeneralResponse> getLectionsByModule(
            @PathVariable Long moduleId,
            HttpServletRequest httpRequest) {
        List<LectionResponse> response = lectionService.getLectionsByModule(moduleId);
        return buildResponse(response, "Lections retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getLectionById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        LectionResponse response = lectionService.getLectionById(id);
        return buildResponse(response, "Lection found.", HttpStatus.OK, httpRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateLection(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLectionRequest request,
            HttpServletRequest httpRequest) {
        LectionResponse response = lectionService.updateLection(id, request);
        return buildResponse(response, "Lection updated successfully.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteLection(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        lectionService.deleteLection(id);
        return buildResponse(null, "Lection deleted successfully.", HttpStatus.OK, httpRequest);
    }
}