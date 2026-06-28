package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.catalogue.Review;
import com.example.cursoapp.dto.catalogue.course.*;
import com.example.cursoapp.dto.catalogue.review.BasicReviewResponse;
import com.example.cursoapp.mapper.catalogue.CourseMapper;
import com.example.cursoapp.mapper.catalogue.TagMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.catalogue.ReviewRepository;
import com.example.cursoapp.service.catalogue.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    private Course getOrThrowById(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalStateException("The ID provided does not belong to any existing Course.") // () -> new ResourceNotFoundException("Course not found with id: " + courseId)
        );
    }

    private Pair<Double, Long> getBasicStats(UUID courseId) {
        //TODO: Implement this method to fetch reviews and calculate average rating and total reviews
        List<BasicReviewResponse> reviews = List.of(); // reviewService.findAllBasicReviewsByCourse(courseId);

        Long reviewCount = (long) reviews.size(); // why
        Long totalScore = 0L;

        for (BasicReviewResponse r : reviews) {
            totalScore += r.score().longValue();
        }

        Double averageScore = totalScore.doubleValue() / (reviewCount == 0 ? 1 : reviewCount.doubleValue());
        return Pair.of(averageScore, reviewCount);
    }

    @Override
    @Transactional(readOnly = true)
    public BasicCourseResponse findBasicCourseById(UUID id) {
        Course course = getOrThrowById(id);
        Pair<Double, Long> stats = getBasicStats(id);

        return CourseMapper.toBasicDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                stats.getFirst(),
                stats.getSecond()
        );
    }

    @Override
    public InstructorCourseResponse findInstructorCourseById(UUID id) {
        return null;
    }

    @Override
    public AdminCourseResponse findAdminCourseById(UUID id) {
        return null;
    }

    @Override
    public List<BasicCourseResponse> getAllCourses() {
        return List.of();
    }

    @Override
    public List<AdminCourseResponse> getAllAdminCourses() {
        return List.of();
    }

    @Override
    public List<BasicCourseResponse> getAllCoursesByTag(UUID tagId) {
        return List.of();
    }

    @Override
    public List<AdminCourseResponse> getAllAdminCoursesByTag(UUID tagId) {
        return List.of();
    }

    @Override
    public List<BasicCourseResponse> getAllCoursesByInstructor(UUID instructorId) {
        return List.of();
    }

    @Override
    public List<AdminCourseResponse> getAllAdminCoursesByInstructor(UUID instructorId) {
        return List.of();
    }

    @Override
    public InstructorCourseResponse createCourse(CreateCourseRequest request) {
        return null;
    }

    @Override
    public InstructorCourseResponse updateCourse(UpdateCourseRequest request) {
        return null;
    }

    @Override
    public BasicCourseResponse publishCourse(UUID id) {
        return null;
    }

    @Override
    public BasicCourseResponse unpublishCourse(UUID id) {
        return null;
    }

    @Override
    public BasicCourseResponse deleteCourse(UUID id) {
        return null;
    }

    @Override
    public BasicCourseResponse restoreCourse(UUID id) {
        return null;
    }
}