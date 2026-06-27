package com.example.cursoapp.dto.catalogue.review;

import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AdminReviewResponse(
        UUID id,
        Integer score,
        String comment,
        Instant createdAt,
        Boolean isUpdated,

        // AdminUserResponse userId,
        BasicCourseResponse course,

        Instant lastActionAt,
        String lastActionBy
) {
}
