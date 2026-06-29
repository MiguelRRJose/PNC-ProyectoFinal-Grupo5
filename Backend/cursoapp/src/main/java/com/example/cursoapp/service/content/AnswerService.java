package com.example.cursoapp.service.content;

import com.example.cursoapp.dto.content.answer.AnswerResponse;
import com.example.cursoapp.dto.content.answer.CreateAnswerRequest;

import java.util.List;

public interface AnswerService {
    AnswerResponse createAnswer(CreateAnswerRequest request, Long instructorId);
    List<AnswerResponse> getAnswersByQuestion(Long questionId);
    AnswerResponse getAnswerById(Long id);
    void deleteAnswer(Long id);
}