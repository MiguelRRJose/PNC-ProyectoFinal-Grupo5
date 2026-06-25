package com.example.cursoapp.service;

import com.example.cursoapp.dto.response.CompletionResponse;

import java.util.List;

public interface CompletionService {
    CompletionResponse markAsCompleted(Long userId, Long lectionId);
    List<CompletionResponse> getCompletionsByUser(Long userId);
    List<CompletionResponse> getCompletionsByLection(Long lectionId);
}