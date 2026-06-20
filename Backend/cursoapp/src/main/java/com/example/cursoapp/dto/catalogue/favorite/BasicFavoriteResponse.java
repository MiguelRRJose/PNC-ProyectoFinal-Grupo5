package com.example.cursoapp.dto.catalogue.favorite;

import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;

import java.util.UUID;

public record BasicFavoriteResponse(
        UUID id,
        BasicCourseResponse course
) {
}