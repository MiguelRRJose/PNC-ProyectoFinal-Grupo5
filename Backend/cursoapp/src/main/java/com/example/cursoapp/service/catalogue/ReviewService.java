package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.review.BasicReviewResponse;
import com.example.cursoapp.dto.catalogue.review.CreateReviewRequest;
import com.example.cursoapp.dto.catalogue.review.UpdateReviewRequest;

import java.util.List;
import java.util.UUID;

public interface ReviewService{
    BasicReviewResponse getBasicReviewById(UUID reviewId);
    List<BasicReviewResponse> getAllReviewsByCourse(UUID courseId);
    BasicReviewResponse createReview(CreateReviewRequest request, UUID userId);
    BasicReviewResponse updateReview(UpdateReviewRequest request, UUID reviewId);
    BasicReviewResponse deleteReview(UUID reviewId);
}