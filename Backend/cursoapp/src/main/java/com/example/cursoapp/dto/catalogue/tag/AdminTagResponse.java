package com.example.cursoapp.dto.catalogue.tag;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AdminTagResponse(
        UUID id,
        String currentName,

        // Auditoría
        Instant createdAt,
        Instant lastModifiedAt,
        String lastModifiedBy
) {
}
