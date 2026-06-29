package com.example.cursoapp.dto.content.module;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuleRequest {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotNull(message = "Index is required.")
    @Min(value = 1, message = "Index must be at least 1.")
    private Integer index;

    @NotNull(message = "Course ID is required.")
    private UUID courseId;
}