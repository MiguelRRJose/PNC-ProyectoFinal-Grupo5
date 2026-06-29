package com.example.cursoapp.dto.catalogue.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Builder
public record CreateReviewRequest(
        @NotNull(message = "A score has not been provided.")
        @Range(min = 1, max = 5, message = "The score must be between 1 and 5.")
        Integer score,

        @NotNull(message = "A comment has not been provided.")
        @NotBlank(message = "The comment cannot be empty.")
        String comment,

        // El ID del usuario debería ser obtenida del contexto del JWT,
        // así que no es necesario enviarla en el request

        // Este deberá ser obtenido por medio de un Path Variable.
        @NotNull(message = "A Course ID has not been provided.")
        UUID courseId
) {
}