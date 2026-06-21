package com.example.cursoapp.dto.catalogue.favorite;

import com.example.cursoapp.dto.catalogue.course.AdminCourseResponse;

import java.util.UUID;

public record AdminFavoriteResponse(
        UUID id,
        // AdminUserResponse user, //TODO: When implemented, import it here
        AdminCourseResponse course
) {
}
