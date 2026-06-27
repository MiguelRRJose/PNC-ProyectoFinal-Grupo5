package com.example.cursoapp.dto.catalogue.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTagRequest(
        @NotNull(message = "A name for the tag has not been provided.")
        @NotBlank(message = "The tag must have a name.")
        String name
) {
}