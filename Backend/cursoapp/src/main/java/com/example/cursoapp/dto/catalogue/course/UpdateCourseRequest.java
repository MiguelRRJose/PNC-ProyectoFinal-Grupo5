package com.example.cursoapp.dto.catalogue.course;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateCourseRequest(
        String name,

        @DecimalMin(value="0.0", message = "The price cannot be negative.")
        Double price,

        List<Long> tagIds

        // Los atributos isPublished e isDeleted serán manejados en endpoints dedicados.
) {
}