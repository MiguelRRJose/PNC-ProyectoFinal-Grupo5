package com.example.cursoapp.dto.catalogue.favorite;

import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import lombok.Builder;

@Builder
public record BasicFavoriteResponse(
        Long id,
        BasicCourseResponse course
) {
}