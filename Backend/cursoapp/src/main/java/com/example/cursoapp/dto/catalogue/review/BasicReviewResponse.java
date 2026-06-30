package com.example.cursoapp.dto.catalogue.review;

import com.example.cursoapp.dto.identity.user.UserResponse;
import lombok.Builder;

import java.time.Instant;

@Builder
public record BasicReviewResponse(
        Long id,
        String username,
        Long score,
        String comment,
        Instant createdAt,
        UserResponse user,

        // Supongo que los Reviews solo serán mostrados donde sea que se muestre el curso.
        // Por lo tanto, no es necesario que se muestre el ID o un BasicResponse del curso

        // Similar a cómo funciona YouTube, si un comentario a sido actualizado, que muestre que se ha cambiado.
        Boolean isUpdated
) {
}