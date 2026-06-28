package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.course.*;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    BasicCourseResponse findBasicCourseById(UUID id);
    InstructorCourseResponse findInstructorCourseById(UUID id);
    AdminCourseResponse findAdminCourseById(UUID id);

    List<BasicCourseResponse> getAllCourses();
    List<AdminCourseResponse> getAllAdminCourses();

    List<BasicCourseResponse> getAllCoursesByTag(UUID tagId);
    List<AdminCourseResponse> getAllAdminCoursesByTag(UUID tagId);

    List<BasicCourseResponse> getAllCoursesByInstructor(UUID instructorId);
    List<AdminCourseResponse> getAllAdminCoursesByInstructor(UUID instructorId);

    InstructorCourseResponse createCourse(CreateCourseRequest request);

    InstructorCourseResponse updateCourse(UUID courseId, UpdateCourseRequest request);

    BasicCourseResponse publishCourse(UUID id);
    BasicCourseResponse unpublishCourse(UUID id);

    BasicCourseResponse deleteCourse(UUID id); // Esto usa un soft delete
    BasicCourseResponse restoreCourse(UUID id); // ¿Ven? Se puede restablecer (siempre que no se borre de verdadJ)
}