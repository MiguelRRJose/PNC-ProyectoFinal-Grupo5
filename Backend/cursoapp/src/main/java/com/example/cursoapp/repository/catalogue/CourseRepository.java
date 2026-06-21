package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<UUID, Course> {
}