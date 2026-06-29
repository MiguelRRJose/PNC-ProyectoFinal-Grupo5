package com.example.cursoapp.dto.catalogue.course;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UpdateCourseRequest(
        String name,

        @DecimalMin(value="0.0", message = "The price cannot be negative.")
        Double price,

        @NotEmpty(message = "The Course requires at least one tag.")
        List<UUID> tagIds

        // Los atributos isPublished e isDeleted serán manejados en endpoints
        // dedicados.
) {
}