package com.example.cursoapp.dto.content.file;

import jakarta.validation.constraints.*;
import lombok.*;

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