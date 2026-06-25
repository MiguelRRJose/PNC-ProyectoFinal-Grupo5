package com.example.cursoapp.controller;

import com.example.cursoapp.dto.response.CompletionResponse;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.service.impl.CompletionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/completions")
@RequiredArgsConstructor
public class CompletionController {

    private final CompletionServiceImpl completionService;

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

    @PostMapping("/user/{userId}/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> markAsCompleted(
            @PathVariable Long userId,
            @PathVariable Long lectionId,
            HttpServletRequest httpRequest) {
        CompletionResponse response = completionService.markAsCompleted(userId, lectionId);
        return buildResponse(response, "Lection marked as completed.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GeneralResponse> getCompletionsByUser(
            @PathVariable Long userId,
            HttpServletRequest httpRequest) {
        List<CompletionResponse> response = completionService.getCompletionsByUser(userId);
        return buildResponse(response, "Completions retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/lection/{lectionId}")
    public ResponseEntity<GeneralResponse> getCompletionsByLection(
            @PathVariable Long lectionId,
            HttpServletRequest httpRequest) {
        List<CompletionResponse> response = completionService.getCompletionsByLection(lectionId);
        return buildResponse(response, "Completions retrieved successfully.", HttpStatus.OK, httpRequest);
    }
}