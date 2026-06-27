package com.example.cursoapp.repository;

import com.example.cursoapp.domain.entity.content.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
    boolean existsByQuestionId(Long questionId);
}
