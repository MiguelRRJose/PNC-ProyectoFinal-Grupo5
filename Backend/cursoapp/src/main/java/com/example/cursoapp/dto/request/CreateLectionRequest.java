package com.example.cursoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLectionRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    private String content;

    @NotNull(message = "Module ID is required.")
    private Long moduleId;
}