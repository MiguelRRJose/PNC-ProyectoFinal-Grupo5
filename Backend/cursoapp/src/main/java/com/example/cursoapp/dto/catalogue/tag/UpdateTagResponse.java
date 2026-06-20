package com.example.cursoapp.dto.catalogue.tag;

import jakarta.validation.constraints.NotBlank;

public record UpdateTagResponse(
        @NotBlank(message = "The tag must have a name.")
        String name
) {
}