package com.example.cursoapp.dto.catalogue.review;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record UpdateReviewRequest(
        @Range(min = 1, max = 5, message = "The score must be between 1 and 5.")
        Long score,

        @NotBlank(message = "The comment cannot be empty.")
        String comment
) {
}
