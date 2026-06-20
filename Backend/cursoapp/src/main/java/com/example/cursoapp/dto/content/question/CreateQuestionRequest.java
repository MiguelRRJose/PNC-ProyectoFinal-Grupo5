package com.example.cursoapp.dto.content.question;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {

    @NotBlank(message = "Content is required.")
    private String content;

    @NotNull(message = "Lection ID is required.")
    private Long lectionId;
}