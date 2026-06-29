package com.example.cursoapp.mapper.progress;

import com.example.cursoapp.domain.entity.progress.Completion;
import com.example.cursoapp.dto.progress.completion.CompletionResponse;
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
