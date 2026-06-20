package com.example.cursoapp.dto.catalogue.favorite;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Dado que "Favorite" es un estado que un usuario asigna a un curso...
// No es algo que pueda ser actualizado si no es que para desmarcar como favorito

public record CreateFavoriteRequest(
        // Nuevamente, se supone que el contexto del JWT debería tener
        // el ID del usuario, así que solo sería necesario el ID del curso
        @NotNull(message = "A Course ID has not been provided.")
        UUID courseId
) {
}