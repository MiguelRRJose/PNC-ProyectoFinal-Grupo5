package com.example.cursoapp.repository.progress;

import com.example.cursoapp.domain.entity.progress.Certified;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertifiedRepository extends JpaRepository<Certified, Long> {
    List<Certified> findByUserId(Long userId);
    List<Certified> findByCourseId(Long courseId);
    Optional<Certified> findByUserIdAndCourseId(Long userId, Long courseId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
