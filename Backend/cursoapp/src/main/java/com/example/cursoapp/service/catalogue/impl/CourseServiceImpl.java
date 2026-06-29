package com.example.cursoapp.service.catalogue.impl;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.catalogue.Review;
import com.example.cursoapp.dto.catalogue.course.*;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.catalogue.CourseMapper;
import com.example.cursoapp.mapper.catalogue.TagMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.catalogue.ReviewRepository;
import com.example.cursoapp.repository.catalogue.TagRepository;
import com.example.cursoapp.service.catalogue.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TagRepository tagRepository;
    private final ReviewRepository reviewRepository;

    private Course getOrThrowById(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course not found with id: " + courseId)
        );
    }

    private Pair<Double, Long> getBasicStats(UUID courseId) {
        List<Review> reviews = reviewRepository.findByCourseId(courseId);
        long reviewCount = reviews.size();
        long totalScore = 0L;
            for (Review r : reviews) {
                if (r.getScore() != null) totalScore += r.getScore();
            }
        Double averageScore = (reviewCount == 0) ? 0.0 : ((double) totalScore / (double) reviewCount);
        return Pair.of(averageScore, reviewCount);
    }

    // Obtener cursos por ID

    @Override
    @Transactional(readOnly = true)
    public BasicCourseResponse findBasicCourseById(UUID id) {
        Course course = getOrThrowById(id);
        Pair<Double, Long> stats = getBasicStats(id);

        if (!course.getIsDeleted() && course.getIsPublished()) {
            return CourseMapper.toBasicDTO(
                    course,
                    course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                    stats.getFirst(),
                    stats.getSecond()
            );
        } else throw new ResourceNotFoundException("Course not found with id: " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorCourseResponse findInstructorCourseById(UUID id) {
        Course course = getOrThrowById(id);
        Pair<Double, Long> stats = getBasicStats(id);

        //TODO: I have to somehow count how many users bought the course, how many have completed it, and well, how much revenue the Instructor got of it
        Integer enrollmentCount = null, certifiedCount = null;
        Double totalRevenue = null;

        if (!course.getIsDeleted()) {
            return CourseMapper.toInstructorDTO(
                    course,
                    course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                    stats.getFirst(),
                    stats.getSecond().intValue(),
                    enrollmentCount,
                    certifiedCount,
                    totalRevenue
            );
        } else throw new ResourceNotFoundException("Course not found with id: " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCourseResponse findAdminCourseById(UUID id) {
        Course course = getOrThrowById(id);
        Pair<Double, Long> stats = getBasicStats(id);

        //TODO: I have to somehow count how many users bought the course, how many have completed it, and well, how much revenue the Instructor got of it
        Integer enrollmentCount = null, certifiedCount = null;
        Double totalRevenue = null;
        Instant lastActionAt = null;
        String lastActionBy = null;

        return CourseMapper.toAdminDTO(
                    course,
                    course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                    stats.getFirst(),
                    stats.getSecond().intValue(),
                    enrollmentCount,
                    certifiedCount,
                    totalRevenue,
                    lastActionAt,
                    lastActionBy
        );
    }

    // Obtener todos los cursos

    //TODO: If I have time, implement a pagination here
    @Override
    @Transactional(readOnly = true)
    public List<BasicCourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findByIsPublishedTrueAndIsDeletedFalse();

        return courses.stream().map(
                (course) -> {
                        Pair<Double, Long> stats = getBasicStats(course.getId());

                        return CourseMapper.toBasicDTO(
                                course,
                                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                                stats.getFirst(),
                                stats.getSecond()
                        );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminCourseResponse> getAllAdminCourses() {
        List<Course> courses = courseRepository.findAll();

        //TODO: Change this later to include the stats
        return courses.stream().map(
                (course) -> {
                        Pair<Double, Long> stats = getBasicStats(course.getId());

                        return CourseMapper.toAdminDTO(
                                course,
                                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                                stats.getFirst(),
                                stats.getSecond().intValue(),
                                null, // enrollmentCount
                                null, // certifiedCount
                                null, // totalRevenue
                                null, // lastActionAt
                                null  // lastActionBy
                        );
                }).toList();
    }

    // Obtener todos los cursos

    @Override
    @Transactional(readOnly = true)
    public List<BasicCourseResponse> getAllCoursesByTag(UUID tagId) {
        List<Course> courses = courseRepository.findCoursesByTagId(tagId);

        return courses.stream().map(
                (course) -> {
                    Pair<Double, Long> stats = getBasicStats(course.getId());

                    return CourseMapper.toBasicDTO(
                            course,
                            course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                            stats.getFirst(),
                            stats.getSecond()
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminCourseResponse> getAllAdminCoursesByTag(UUID tagId) {
        List<Course> courses = courseRepository.findCoursesByTagId(tagId);

        //TODO: Change this later to include the stats
        return courses.stream().map(
                (course) -> {
                    Pair<Double, Long> stats = getBasicStats(course.getId());

                    return CourseMapper.toAdminDTO(
                            course,
                            course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                            stats.getFirst(),
                            stats.getSecond().intValue(),
                            null, // enrollmentCount
                            null, // certifiedCount
                            null, // totalRevenue
                            null, // lastActionAt
                            null  // lastActionBy
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasicCourseResponse> getAllCoursesByInstructor(UUID instructorId) {
        List<Course> courses = courseRepository.findByInstructorIdAndIsPublishedFalseAndIsDeletedFalse(instructorId);

        return courses.stream().map(
                (course) -> {
                    Pair<Double, Long> stats = getBasicStats(course.getId());

                    return CourseMapper.toBasicDTO(
                            course,
                            course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                            stats.getFirst(),
                            stats.getSecond()
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminCourseResponse> getAllAdminCoursesByInstructor(UUID instructorId) {
        List<Course> courses = courseRepository.findByInstructorIdAndIsPublishedFalseAndIsDeletedFalse(instructorId);

        return courses.stream().map(
                (course) -> {
                    Pair<Double, Long> stats = getBasicStats(course.getId());

                    return CourseMapper.toAdminDTO(
                            course,
                            course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                            stats.getFirst(),
                            stats.getSecond().intValue(),
                            null, // enrollmentCount
                            null, // certifiedCount
                            null, // totalRevenue
                            null, // lastActionAt
                            null  // lastActionBy
                    );
                }).toList();
    }

    @Override
    public InstructorCourseResponse createCourse(CreateCourseRequest request) {
        Course course = courseRepository.save(CourseMapper.toCreateEntity(
                request,
                request.tagIds().stream().map((tagId) ->
                        tagRepository.findById(tagId).orElseThrow(
                                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + tagId)
                        )).toList()
        ));

        return CourseMapper.toInstructorDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                0.0,
                0,
                0,
                0,
                0.0
        );
    }

    @Override
    public InstructorCourseResponse updateCourse(UUID courseId, UpdateCourseRequest request) {
        Course course = getOrThrowById(courseId);
        CourseMapper.toUpdateEntity(course, request,
                request.tagIds().stream().map((tagId) ->
                        tagRepository.findById(tagId).orElseThrow(
                                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + tagId)
                        )).toList()
        );

        courseRepository.save(course);

        Pair<Double, Long> stats = getBasicStats(courseId);

        return CourseMapper.toInstructorDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                stats.getFirst(),
                stats.getSecond().intValue(),
                null, // enrollmentCount
                null, // certifiedCount
                null  // totalRevenue
        );
    }

    @Override
    public BasicCourseResponse publishCourse(UUID id) {
        Course course = getOrThrowById(id);
        course.setIsPublished(true);
        courseRepository.save(course);

        Pair<Double, Long> stats = getBasicStats(id);

        return CourseMapper.toBasicDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                stats.getFirst(),
                stats.getSecond()
        );
    }

    @Override
    public BasicCourseResponse unpublishCourse(UUID id) {
        Course course = getOrThrowById(id);
        course.setIsPublished(false);
        courseRepository.save(course);

        Pair<Double, Long> stats = getBasicStats(id);

        return CourseMapper.toBasicDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                stats.getFirst(),
                stats.getSecond()
        );
    }

    @Override
    public BasicCourseResponse deleteCourse(UUID id) {
        Course course = getOrThrowById(id);
        course.setIsDeleted(true);
        courseRepository.save(course);

        Pair<Double, Long> stats = getBasicStats(id);

        return CourseMapper.toBasicDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                stats.getFirst(),
                stats.getSecond()
        );
    }

    @Override
    public BasicCourseResponse restoreCourse(UUID id) {
        Course course = getOrThrowById(id);
        course.setIsDeleted(false);
        courseRepository.save(course);

        Pair<Double, Long> stats = getBasicStats(id);

        return CourseMapper.toBasicDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                stats.getFirst(),
                stats.getSecond()
        );
    }
}