package com.example.cursoapp.service.catalogue;

import com.example.cursoapp.dto.catalogue.course.*;

import java.util.List;

public interface CourseService {
    BasicCourseResponse findBasicCourseById(Long id);
    InstructorCourseResponse findInstructorCourseById(Long id);
    AdminCourseResponse findAdminCourseById(Long id);

    List<BasicCourseResponse> getAllCourses();
    List<AdminCourseResponse> getAllAdminCourses();

    List<BasicCourseResponse> getAllCoursesByTag(Long tagId);
    List<AdminCourseResponse> getAllAdminCoursesByTag(Long tagId);

    List<BasicCourseResponse> getAllCoursesByInstructor(Long instructorId);
    List<AdminCourseResponse> getAllAdminCoursesByInstructor(Long instructorId);

    InstructorCourseResponse createCourse(CreateCourseRequest request);

    InstructorCourseResponse updateCourse(Long courseId, UpdateCourseRequest request);

    BasicCourseResponse publishCourse(Long id);
    BasicCourseResponse unpublishCourse(Long id);

    BasicCourseResponse deleteCourse(Long id); // Esto usa un soft delete
    BasicCourseResponse restoreCourse(Long id); // ¿Ven? Se puede restablecer (siempre que no se borre de verdadJ)
}