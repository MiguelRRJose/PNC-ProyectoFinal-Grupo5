package com.example.cursoapp.dto.catalogue.review;

import java.time.Instant;
import java.util.UUID;

public record BasicReviewResponse(
        UUID id,
        Integer score,
        String comment,
        Instant createdAt,
        // BasicUserResponse user, //TODO: Implementar una vez hecho los usuarios

        // Supongo que los Reviews solo serán mostrados donde sea que se muestre el curso.
        // Por lo tanto, no es necesario que se muestre el ID o un BasicResponse del curso.

        // Similar a cómo funciona YouTube, si un comentario a sido actualizado, que muestre que se ha cambiado.
        Boolean isUpdated
) {
}