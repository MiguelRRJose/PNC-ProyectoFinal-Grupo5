package com.example.cursoapp.dto.content.lection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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