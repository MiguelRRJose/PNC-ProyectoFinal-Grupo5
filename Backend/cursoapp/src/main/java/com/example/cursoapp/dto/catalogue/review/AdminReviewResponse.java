package com.example.cursoapp.dto.catalogue.review;

import java.time.Instant;
import java.util.UUID;

public record AdminReviewResponse(
        UUID id,
        Integer score,
        String comment,
        Instant createdAt,
        Boolean isUpdated,

        UUID userId,
        UUID courseId,

        Instant lastActionAt,
        String lastActionBy
) {
}
