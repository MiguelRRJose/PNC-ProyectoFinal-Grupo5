package com.example.cursoapp.dto.response;

import lombok.*;

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