package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Review;
import com.example.cursoapp.dto.catalogue.review.BasicReviewResponse;
import com.example.cursoapp.dto.catalogue.review.CreateReviewRequest;
import com.example.cursoapp.dto.catalogue.review.UpdateReviewRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.catalogue.ReviewMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.catalogue.ReviewRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.service.catalogue.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UsuarioRepository usuarioRepository;

    public Review getByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    @Override
    public BasicReviewResponse getBasicReviewById(Long reviewId) {
        Review review = getByIdOrThrow(reviewId);
        String username = review.getUser() != null ? review.getUser().getUsername() : null;
        return ReviewMapper.toBasicDTO(review, username);
    }

    @Override
    public List<BasicReviewResponse> getAllReviewsByCourse(Long courseId) {
        List<Review> courseReviews = reviewRepository.findByCourseId(courseId);

        return courseReviews.stream().map(review ->
                ReviewMapper.toBasicDTO(review, review.getUser() != null ? review.getUser().getUsername() : null)
        ).toList();
    }

    @Override
    public BasicReviewResponse createReview(CreateReviewRequest request, Long userId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Review review = reviewRepository.save(
                ReviewMapper.toCreateEntity(request, user, courseRepository.getReferenceById(request.courseId()))
        );

        return ReviewMapper.toBasicDTO(review, user.getUsername());
    }

    @Override
    public BasicReviewResponse updateReview(UpdateReviewRequest request, Long reviewId) {
        Review review = ReviewMapper.toUpdateEntity(getByIdOrThrow(reviewId), request);
        Review saved = reviewRepository.save(review);

        return ReviewMapper.toBasicDTO(
                saved,
                saved.getUser() != null ? saved.getUser().getUsername() : null
        );
    }

    @Override
    public BasicReviewResponse deleteReview(Long reviewId) {
        Review review = getByIdOrThrow(reviewId);
        reviewRepository.delete(review);

        return ReviewMapper.toBasicDTO(
                review,
                review.getUser() != null ? review.getUser().getUsername() : null
        );
    }
}