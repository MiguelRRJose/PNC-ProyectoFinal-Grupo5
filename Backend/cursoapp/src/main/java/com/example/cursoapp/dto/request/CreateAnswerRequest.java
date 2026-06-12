package com.example.cursoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnswerRequest {

    @NotBlank(message = "Content is required.")
    private String content;

    @NotNull(message = "Question ID is required.")
    private Long questionId;
}