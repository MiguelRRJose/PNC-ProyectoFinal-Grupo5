package com.example.cursoapp.repository.progress;

import com.example.cursoapp.domain.entity.progress.Completion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompletionRepository extends JpaRepository<Completion, Long> {
    List<Completion> findByUserId(Long userId);
    List<Completion> findByLectionId(Long lectionId);
    Optional<Completion> findByUserIdAndLectionId(Long userId, Long lectionId);
    List<Completion> findByUserIdAndIsCompleted(Long userId, Boolean isCompleted);
}
