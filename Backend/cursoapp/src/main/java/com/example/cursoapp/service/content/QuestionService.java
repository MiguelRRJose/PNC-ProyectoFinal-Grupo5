package com.example.cursoapp.service.content;

import com.example.cursoapp.dto.content.question.CreateQuestionRequest;
import com.example.cursoapp.dto.content.question.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(CreateQuestionRequest request, Long userId);
    List<QuestionResponse> getQuestionsByLection(Long lectionId);
    List<QuestionResponse> getQuestionsByUser(Long userId);
    QuestionResponse getQuestionById(Long id);
    void deleteQuestion(Long id);
}