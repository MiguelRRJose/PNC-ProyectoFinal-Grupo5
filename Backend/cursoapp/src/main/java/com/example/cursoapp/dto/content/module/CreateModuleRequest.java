package com.example.cursoapp.dto.content.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateModuleRequest {
    @NotNull(message = "course_id is required")
    private Long courseId;

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "index is required")
    private Integer index;
}