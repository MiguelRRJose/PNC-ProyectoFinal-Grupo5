package com.example.cursoapp.mapper.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.catalogue.Review;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.catalogue.course.BasicCourseResponse;
import com.example.cursoapp.dto.catalogue.review.AdminReviewResponse;
import com.example.cursoapp.dto.catalogue.review.BasicReviewResponse;
import com.example.cursoapp.dto.catalogue.review.CreateReviewRequest;
import com.example.cursoapp.dto.catalogue.review.UpdateReviewRequest;

import java.time.Instant;

public class ReviewMapper {
    public static BasicReviewResponse toBasicDTO (
            Review review,
            String username // Hay que obtener el username desde el servicio
    ) {
        return BasicReviewResponse.builder()
                .id(review.getId())
                .username(username)
                .score(review.getScore())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .isUpdated(review.getIsUpdated())
                .build();
    }

    public static AdminReviewResponse toAdminDTO (
            Review review,
            // AdminUserResponse user,
            BasicCourseResponse course

    ) {
        return AdminReviewResponse.builder()
                .id(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .isUpdated(review.getIsUpdated())
                // .userId(user)
                .course(course)
                .build();
    }

    public static Review toCreateEntity (
            CreateReviewRequest createRequest,
            Usuario user, // Hay que obtener el usuario desde el servicio
            Course course // Hay que obtener el curso desde el servicio
    ) {
        return Review.builder()
                .score(createRequest.score())
                .comment(createRequest.comment())
                .user(user)
                .course(course)
                .build();
    }

    public static Review toUpdateEntity (
            Review entity,
            UpdateReviewRequest updateRequest
    ) {
        if (updateRequest.score() != null) entity.setScore(updateRequest.score());
        if (updateRequest.comment() != null) entity.setComment(updateRequest.comment());
        entity.setIsUpdated(true);

        return entity;
    }
}