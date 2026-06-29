package com.example.cursoapp.dto.catalogue.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateTagRequest(
        @NotBlank(message = "The tag must have a name.")
        String name
) {
}