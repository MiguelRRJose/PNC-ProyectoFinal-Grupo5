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
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.catalogue.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final UsuarioRepository userRepository;
    private final CourseRepository courseRepository;
    private final TagRepository tagRepository;
    private final ReviewRepository reviewRepository;

    private Course getOrThrowById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course not found with id: " + courseId)
        );
    }

    private Pair<Double, Long> getBasicStats(Long courseId) {
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
    public BasicCourseResponse findBasicCourseById(Long id) {
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
    public InstructorCourseResponse findInstructorCourseById(Long id) {
        Course course = getOrThrowById(id);
        Pair<Double, Long> stats = getBasicStats(id);

        //TODO: I have to somehow count how many users bought the course, how many have completed it, and well, how much revenue the Instructor got of it
        Long enrollmentCount = null, certifiedCount = null;
        Double totalRevenue = null;

        if (!course.getIsDeleted()) {
            return CourseMapper.toInstructorDTO(
                    course,
                    course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                    stats.getFirst(),
                    stats.getSecond(),
                    enrollmentCount,
                    certifiedCount,
                    totalRevenue
            );
        } else throw new ResourceNotFoundException("Course not found with id: " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCourseResponse findAdminCourseById(Long id) {
        Course course = getOrThrowById(id);
        Pair<Double, Long> stats = getBasicStats(id);

        //TODO: I have to somehow count how many users bought the course, how many have completed it, and well, how much revenue the Instructor got of it
        Long enrollmentCount = null, certifiedCount = null;
        Double totalRevenue = null;

        return CourseMapper.toAdminDTO(
                    course,
                    course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                    stats.getFirst(),
                    stats.getSecond(),
                    enrollmentCount,
                    certifiedCount,
                    totalRevenue
        );
    }

    // Obtener todos los cursos

    @Override
    @Transactional(readOnly = true)
    public List<BasicCourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findByIsPublishedAndIsDeleted(true, false);

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
                                stats.getSecond(),
                                null, // enrollmentCount
                                null, // certifiedCount
                                null // totalRevenue
                        );
                }).toList();
    }

    // Obtener todos los cursos

    @Override
    @Transactional(readOnly = true)
    public List<BasicCourseResponse> getAllCoursesByTag(Long tagId) {
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
    public List<AdminCourseResponse> getAllAdminCoursesByTag(Long tagId) {
        List<Course> courses = courseRepository.findCoursesByTagId(tagId);

        //TODO: Change this later to include the stats
        return courses.stream().map(
                (course) -> {
                    Pair<Double, Long> stats = getBasicStats(course.getId());

                    return CourseMapper.toAdminDTO(
                            course,
                            course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                            stats.getFirst(),
                            stats.getSecond(),
                            null, // enrollmentCount
                            null, // certifiedCount
                            null // totalRevenue
                    );
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasicCourseResponse> getAllCoursesByInstructor(Long instructorId) {
        List<Course> courses = courseRepository.findCourseByInstructor_Id(instructorId);

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
    public List<AdminCourseResponse> getAllAdminCoursesByInstructor(Long instructorId) {
        List<Course> courses = courseRepository.findCourseByInstructor_Id(instructorId);

        return courses.stream().map(
                (course) -> {
                    Pair<Double, Long> stats = getBasicStats(course.getId());

                    return CourseMapper.toAdminDTO(
                            course,
                            course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                            stats.getFirst(),
                            stats.getSecond(),
                            null, // enrollmentCount
                            null, // certifiedCount
                            null // totalRevenue
                    );
                }).toList();
    }

    @Override
    public InstructorCourseResponse createCourse(CreateCourseRequest request, Long instructorId) {

        Course course = courseRepository.save(CourseMapper.toCreateEntity(
                request,
                request.tagIds().stream().map((tagId) ->
                        tagRepository.findById(tagId).orElseThrow(
                                () -> new IllegalStateException("The ID provided does not belong to any existing Tag.") // () -> new ResourceNotFoundException("Tag not found with id: " + tagId)
                        )).toList(),
                userRepository.getReferenceById(instructorId)
        ));

        return CourseMapper.toInstructorDTO(
                course,
                course.getTags().stream().map(TagMapper::toBasicDTO).toList(),
                0.0, 0L, 0L, 0L, 0.0
        );
    }

    @Override
    public InstructorCourseResponse updateCourse(Long courseId, UpdateCourseRequest request) {
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
                stats.getSecond(),
                null, // enrollmentCount
                null, // certifiedCount
                null  // totalRevenue
        );
    }

    @Override
    public BasicCourseResponse publishCourse(Long id) {
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
    public BasicCourseResponse unpublishCourse(Long id) {
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
    public BasicCourseResponse deleteCourse(Long id) {
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
    public BasicCourseResponse restoreCourse(Long id) {
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