package com.example.cursoapp.dto.catalogue.tag;

import java.time.Instant;
import java.util.UUID;

public record AdminTagResponse(
        UUID id,
        String currentName,

        // Auditoría
        Instant createdAt,
        Instant lastModifiedAt,
        String lastModifiedBy,

        // No estoy seguro si considerar a este objeto como tal
        // O si mejor manejarlo desde los logs
        BasicTagResponse tagBefore

) {
}
