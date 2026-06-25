package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.Completion;
import com.example.cursoapp.dto.response.CompletionResponse;
import org.springframework.stereotype.Component;

@Component
public class CompletionMapper {

    public CompletionResponse toDto(Completion completion) {
        return CompletionResponse.builder()
                .id(completion.getId())
                .userId(completion.getUser().getId())
                .lectionId(completion.getLection().getId())
                .isCompleted(completion.getIsCompleted())
                .build();
    }
}