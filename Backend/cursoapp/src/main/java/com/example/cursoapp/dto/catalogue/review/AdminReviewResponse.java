package com.example.cursoapp.dto.catalogue.review;

import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import com.example.cursoapp.dto.identity.user.UserResponse;
import lombok.Builder;

import java.time.Instant;

@Builder
public record AdminReviewResponse(
        Long id,
        Long score,
        String comment,
        Instant createdAt,
        Boolean isUpdated,

        UserResponse userId,
        BasicCourseResponse course
) {
}
