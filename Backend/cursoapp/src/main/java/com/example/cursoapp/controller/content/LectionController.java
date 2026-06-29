package com.example.cursoapp.controller.content;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.content.lection.CreateLectionRequest;
import com.example.cursoapp.dto.content.lection.UpdateLectionRequest;
import com.example.cursoapp.dto.content.lection.LectionResponse;
import com.example.cursoapp.service.content.LectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/lections")
@RequiredArgsConstructor
public class LectionController {

    private final LectionService lectionService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status).body(
                GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(Instant.now())
                        .data(data)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createLection(@Valid @RequestBody CreateLectionRequest request) {
        return buildResponse(lectionService.createLection(request), "Lection created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<GeneralResponse> getLectionsByModule(@PathVariable Long moduleId) {
        return buildResponse(lectionService.getLectionsByModule(moduleId), "Lections retrieved successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getLectionById(@PathVariable Long id) {
        return buildResponse(lectionService.getLectionById(id), "Lection found.", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateLection(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateLectionRequest request) {
        return buildResponse(lectionService.updateLection(id, request), "Lection updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteLection(@PathVariable Long id) {
        lectionService.deleteLection(id);
        return buildResponse(null, "Lection deleted successfully.", HttpStatus.OK);
    }
}