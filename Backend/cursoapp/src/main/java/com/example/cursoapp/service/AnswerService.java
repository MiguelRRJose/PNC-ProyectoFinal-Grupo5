package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreateAnswerRequest;
import com.example.cursoapp.dto.response.AnswerResponse;

import java.util.List;

public interface AnswerService {
    AnswerResponse createAnswer(CreateAnswerRequest request, Long instructorId);
    List<AnswerResponse> getAnswersByQuestion(Long questionId);
    AnswerResponse getAnswerById(Long id);
    void deleteAnswer(Long id);
}