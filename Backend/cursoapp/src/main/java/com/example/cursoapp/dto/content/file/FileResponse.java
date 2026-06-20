package com.example.cursoapp.dto.content.file;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private Long id;
    private String pathToFile;
    private Long lectionId;
}