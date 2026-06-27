package com.example.cursoapp.dto.content.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAnswerRequest {
    @NotNull(message = "question_id is required")
    private Long questionId;

    @NotBlank(message = "content is required")
    private String content;
}