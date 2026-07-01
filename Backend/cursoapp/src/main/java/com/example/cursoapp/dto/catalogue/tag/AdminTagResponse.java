package com.example.cursoapp.dto.catalogue.tag;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AdminTagResponse(
        Long id,
        String currentName,

        // Auditoría
        Instant createdAt
) {
}
