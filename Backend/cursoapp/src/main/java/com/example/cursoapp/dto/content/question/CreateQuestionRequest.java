package com.example.cursoapp.dto.content.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateQuestionRequest {
    @NotNull(message = "lection_id is required")
    private Long lectionId;

    @NotBlank(message = "content is required")
    private String content;
}
