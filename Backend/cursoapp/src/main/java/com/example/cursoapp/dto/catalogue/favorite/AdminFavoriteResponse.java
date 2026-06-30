package com.example.cursoapp.dto.catalogue.favorite;

import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import com.example.cursoapp.dto.identity.user.UserResponse;
import lombok.Builder;

@Builder
public record AdminFavoriteResponse(
        Long id,
        UserResponse user,

        // Creo que el administrador no necesita toda la información del curso, solo la básica,
        // ya que el administrador puede ver toda la información del curso en otra sección.
        BasicCourseResponse course
) {
}
