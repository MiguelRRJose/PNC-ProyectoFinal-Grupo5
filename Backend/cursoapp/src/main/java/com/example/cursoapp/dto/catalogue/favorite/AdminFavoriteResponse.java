package com.example.cursoapp.dto.catalogue.favorite;

import com.example.cursoapp.dto.catalogue.course.AdminCourseResponse;
import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AdminFavoriteResponse(
        UUID id,
        // AdminUserResponse user, //TODO: When implemented, import it here

        // Creo que el administrador no necesita toda la información del curso, solo la básica,
        // ya que el administrador puede ver toda la información del curso en otra sección.
        BasicCourseResponse course
) {
}
