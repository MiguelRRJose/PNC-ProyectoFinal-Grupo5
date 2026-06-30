package com.example.cursoapp.repository.catalogue;

import com.example.cursoapp.domain.entity.catalogue.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourseId(Long courseId);
}