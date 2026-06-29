package com.example.cursoapp.dto.progress.completion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionResponse {
    private Long id;
    private Long userId;
    private Long lectionId;
    private Boolean isCompleted;
}
