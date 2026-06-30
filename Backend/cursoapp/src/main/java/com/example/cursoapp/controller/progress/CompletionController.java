package com.example.cursoapp.controller.progress;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.progress.completion.CreateCompletionRequest;
import com.example.cursoapp.service.progress.CompletionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.cursoapp.config.UsuarioDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;

@RestController
@RequestMapping("/api/completions")
@RequiredArgsConstructor
public class CompletionController {

    private final CompletionService completionService;

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

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(
                completionService.getCompletionById(id),
                "Completion successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-user")
    public ResponseEntity<GeneralResponse> getByUser(@RequestParam Long userId) {
        return buildResponse(
                completionService.getCompletionsByUser(userId),
                "Completions successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/completed-by-user")
    public ResponseEntity<GeneralResponse> getCompletedByUser(@RequestParam Long userId) {
        return buildResponse(
                completionService.getCompletedByUser(userId),
                "Completed lections successfully found.",
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> markAsCompleted(@RequestBody @Valid CreateCompletionRequest request) {
        UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = usuarioDetails.getId();
        return buildResponse(
                completionService.markAsCompleted(request, userId),
                "Lection successfully marked as completed.",
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/incomplete")
    public ResponseEntity<GeneralResponse> markAsIncomplete(@RequestParam Long lectionId) {
        UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = usuarioDetails.getId();
        return buildResponse(
                completionService.markAsIncomplete(userId, lectionId),
                "Lection successfully marked as incomplete.",
                HttpStatus.OK
        );
    }
}
