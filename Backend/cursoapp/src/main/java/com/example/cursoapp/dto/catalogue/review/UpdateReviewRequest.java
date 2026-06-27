package com.example.cursoapp.dto.catalogue.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

public record UpdateReviewRequest(
        @Range(min = 1, max = 5, message = "The score must be between 1 and 5.")
        Integer score,

        @NotBlank(message = "The comment cannot be empty.")
        String comment,

        // Este deberá ser obtenido por medio de un Path Variable.
        @NotNull(message = "A Course ID has not been provided.")
        UUID courseId
) {
}
