package com.example.cursoapp.repository;

import com.example.cursoapp.domain.entity.content.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByLectionId(Long lectionId);
    List<Question> findByUserId(Long userId);
}