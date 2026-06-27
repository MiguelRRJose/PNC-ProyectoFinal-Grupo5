package com.example.cursoapp.dto.catalogue.favorite;

import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BasicFavoriteResponse(
        UUID id,
        BasicCourseResponse course
) {
}