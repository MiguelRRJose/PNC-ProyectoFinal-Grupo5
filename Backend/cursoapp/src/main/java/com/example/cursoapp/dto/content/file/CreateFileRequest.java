package com.example.cursoapp.dto.content.file;

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
public class CreateFileRequest {

    @NotBlank(message = "File path is required.")
    private String pathToFile;

    @NotNull(message = "Lection ID is required.")
    private Long lectionId;
}