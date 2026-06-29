package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    @Query("SELECT DISTINCT c FROM Course c JOIN FETCH c.tags t WHERE t.id = :tagId")
    List<Course> findCoursesByTagId(@Param("tagId") UUID tagId);

    //Claude me dijo que esto es legal, y que JPA hace la consulta automáticamente. Será de ver...

    List<Course> findByIsPublishedTrueAndIsDeletedFalse();

    List<Course> findByIsDeletedTrue();

    List<Course> findByInstructorIdAndIsPublishedFalseAndIsDeletedFalse(UUID instructorId);
}