package com.example.cursoapp.controller.catalogue;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.catalogue.review.CreateReviewRequest;
import com.example.cursoapp.dto.catalogue.review.UpdateReviewRequest;
import com.example.cursoapp.service.catalogue.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(Instant.now())
                        .data(data)
                        .build()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(
                reviewService.getBasicReviewById(id),
                "Review successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-course")
    public ResponseEntity<GeneralResponse> getByCourseId(@RequestParam Long courseId) {
        return buildResponse(
                reviewService.getAllReviewsByCourse(courseId),
                "Reviews successfully found.",
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createReview(@RequestBody CreateReviewRequest request) {

        //TODO: Get the user ID somehow using the JWT context
        Long userId = null;

        return buildResponse(
                reviewService.createReview(request, userId),
                "Review successfully created.",
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateReview(@PathVariable Long id, @RequestBody UpdateReviewRequest request) {
        return buildResponse(
                reviewService.updateReview(request, id),
                "Review successfully updated.",
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteReview(@PathVariable Long id) {
        return buildResponse(
                reviewService.deleteReview(id),
                "Review successfully deleted.",
                HttpStatus.OK
        );
    }
}

