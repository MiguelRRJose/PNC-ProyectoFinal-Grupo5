package com.example.cursoapp.dto.catalogue.course;

import com.example.cursoapp.domain.entity.catalogue.Tag;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record CreateCourseRequest (
        @NotNull(message = "No Name has been received.")
        @NotBlank(message = "The Course requires a name.")
        String name,

        @NotNull(message = "No Price has been received")
        @DecimalMin(value="0.0", message = "The price cannot be negative.")
        Double price,

        // Supuestamente, el ID del usuario que lo creó se puede obtener del contexto JWT
        // en el servicio, no en el controlador o directamente del cliente.

        @NotNull(message = "No Tag list has been received.")
        @NotEmpty(message = "The Course requires at least one tag.")
        List<UUID> tagIds
        // It sure would suck to do the integration for this one!
) {
}