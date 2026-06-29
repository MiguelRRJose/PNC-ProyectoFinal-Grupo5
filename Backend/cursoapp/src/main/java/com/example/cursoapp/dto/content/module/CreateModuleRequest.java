package com.example.cursoapp.dto.content.module;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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