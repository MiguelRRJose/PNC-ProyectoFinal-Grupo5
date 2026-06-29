package com.example.cursoapp.service.progress;

import com.example.cursoapp.dto.progress.completion.CompletionResponse;
import com.example.cursoapp.dto.progress.completion.CreateCompletionRequest;

import java.util.List;

public interface CompletionService {
    CompletionResponse getCompletionById(Long id);
    List<CompletionResponse> getCompletionsByUser(Long userId);
    List<CompletionResponse> getCompletedByUser(Long userId);
    CompletionResponse markAsCompleted(CreateCompletionRequest request, Long userId);
    CompletionResponse markAsIncomplete(Long userId, Long lectionId);
}
