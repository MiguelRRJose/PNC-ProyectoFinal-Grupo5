package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.review.BasicReviewResponse;
import com.example.cursoapp.dto.catalogue.review.CreateReviewRequest;
import com.example.cursoapp.dto.catalogue.review.UpdateReviewRequest;

import java.util.List;

public interface ReviewService{
    BasicReviewResponse getBasicReviewById(Long reviewId);
    List<BasicReviewResponse> getAllReviewsByCourse(Long courseId);
    BasicReviewResponse createReview(CreateReviewRequest request, Long userId);
    BasicReviewResponse updateReview(UpdateReviewRequest request, Long reviewId);
    BasicReviewResponse deleteReview(Long reviewId);
}