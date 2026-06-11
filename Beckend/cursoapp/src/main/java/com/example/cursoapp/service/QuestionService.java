package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreateQuestionRequest;
import com.example.cursoapp.dto.response.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(CreateQuestionRequest request, Long userId);
    List<QuestionResponse> getQuestionsByLection(Long lectionId);
    List<QuestionResponse> getQuestionsByUser(Long userId);
    QuestionResponse getQuestionById(Long id);
    void deleteQuestion(Long id);
}