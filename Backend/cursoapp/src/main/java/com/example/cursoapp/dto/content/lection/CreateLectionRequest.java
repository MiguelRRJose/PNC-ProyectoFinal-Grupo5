package com.example.cursoapp.dto.content.lection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateLectionRequest {
    @NotNull(message = "module_id is required")
    private Long moduleId;

    @NotBlank(message = "title is required")
    private String title;

    private String content;
}