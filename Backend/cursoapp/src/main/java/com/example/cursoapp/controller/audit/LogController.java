package com.example.cursoapp.controller.audit;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.service.audit.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

// Solo ADMIN debería tener acceso a los logs
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

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
                logService.getAllLogs(),
                "Logs successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(
                logService.getLogById(id),
                "Log successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-user")
    public ResponseEntity<GeneralResponse> getByUser(@RequestParam Long userId) {
        return buildResponse(
                logService.getLogsByUser(userId),
                "Logs successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-entity-type")
    public ResponseEntity<GeneralResponse> getByEntityType(@RequestParam Long entityTypeId) {
        return buildResponse(
                logService.getLogsByEntity(entityTypeId),
                "Logs successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-entity")
    public ResponseEntity<GeneralResponse> getByEntityId(@RequestParam Long entityId) {
        return buildResponse(
                logService.getLogsByEntityId(entityId),
                "Logs successfully found.",
                HttpStatus.OK
        );
    }
}
