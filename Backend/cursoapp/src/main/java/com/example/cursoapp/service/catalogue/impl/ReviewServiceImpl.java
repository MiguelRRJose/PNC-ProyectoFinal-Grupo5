package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Review;
import com.example.cursoapp.dto.catalogue.review.BasicReviewResponse;
import com.example.cursoapp.dto.catalogue.review.CreateReviewRequest;
import com.example.cursoapp.dto.catalogue.review.UpdateReviewRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.catalogue.ReviewMapper;
import com.example.cursoapp.repository.catalogue.ReviewRepository;
import com.example.cursoapp.service.catalogue.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    // TODO: When users are done, get the username for the review
    //private final UserRepository userRepository;

    public Review getByIdOrThrow(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    @Override
    public BasicReviewResponse getBasicReviewById(UUID reviewId) {
        Review review = getByIdOrThrow(reviewId);

        //TODO: Here, change the null to the commented code, make necessary changes for it to work
        String username = null; // String username = userRepository.getById(review.getUserId()).getUsername();

        return ReviewMapper.toBasicDTO(review, username);
    }


    @Override
    public List<BasicReviewResponse> getAllReviewsByCourse(UUID courseId) {
        List<Review> courseReviews = reviewRepository.findByCourseId(courseId);

        return courseReviews.stream().map(review ->
                        ReviewMapper.toBasicDTO(review, null) //TODO: Later add the logic to get the usernames
        ).toList();
    }

    @Override
    public BasicReviewResponse createReview(CreateReviewRequest request, UUID userId) {
        //TODO: Later add the logic to get the username

        // String username = userRepository.getById(userId).getUsername();
        Review review = reviewRepository.save(
                ReviewMapper.toCreateEntity(null, request)
        );

        return ReviewMapper.toBasicDTO(review, null);
    }

    @Override
    public BasicReviewResponse updateReview(UpdateReviewRequest request, UUID reviewId) {
        Review review = ReviewMapper.toUpdateEntity(getByIdOrThrow(reviewId), request);

        return ReviewMapper.toBasicDTO(
                reviewRepository.save(review),
                null // userRepository.getById(userId).getUsername()
        ); //TODO: I also need the username here
    }

    @Override
    public BasicReviewResponse deleteReview(UUID reviewId) {
        Review review = getByIdOrThrow(reviewId);
        reviewRepository.delete(review);

        return ReviewMapper.toBasicDTO(
                reviewRepository.save(review),
                null // userRepository.getById(userId).getUsername()
        ); //TODO: And here too, I need the username
    }
}