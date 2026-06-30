package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT DISTINCT c FROM Course c JOIN FETCH c.tags t WHERE t.id = :tagId")
    List<Course> findCoursesByTagId(@Param("tagId") Long tagId);

    //Claude me dijo que esto es legal, y que JPA hace la consulta automáticamente. Será de ver...

    List<Course> findByIsDeletedTrue();

    List<Course> findByInstructorIdAndIsPublishedFalseAndIsDeletedFalse(Long instructorId);

    List<Course> findByIsPublishedAndIsDeleted(Boolean isPublished, Boolean isDeleted);
}