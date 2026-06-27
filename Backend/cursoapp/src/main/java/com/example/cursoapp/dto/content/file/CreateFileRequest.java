package com.example.cursoapp.dto.content.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFileRequest {
    @NotNull(message = "lection_id is required")
    private Long lectionId;

    @NotBlank(message = "path_to_file is required")
    private String pathToFile;
}