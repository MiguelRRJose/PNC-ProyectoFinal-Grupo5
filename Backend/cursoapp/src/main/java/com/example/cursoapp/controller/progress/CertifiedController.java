package com.example.cursoapp.controller.progress;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.progress.certified.CreateCertifiedRequest;
import com.example.cursoapp.service.progress.CertifiedService;
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
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertifiedController {

    private final CertifiedService certifiedService;

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
                certifiedService.getCertifiedById(id),
                "Certificate successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-user")
    public ResponseEntity<GeneralResponse> getByUser(@RequestParam Long userId) {
        return buildResponse(
                certifiedService.getCertifiedByUser(userId),
                "Certificates successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-course")
    public ResponseEntity<GeneralResponse> getByCourse(@RequestParam Long courseId) {
        return buildResponse(
                certifiedService.getCertifiedByCourse(courseId),
                "Certificates successfully found.",
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createCertified(@RequestBody @Valid CreateCertifiedRequest request) {
        UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = usuarioDetails.getId();
        return buildResponse(
                certifiedService.createCertified(request, userId),
                "Certificate successfully issued.",
                HttpStatus.CREATED
        );
    }
}
