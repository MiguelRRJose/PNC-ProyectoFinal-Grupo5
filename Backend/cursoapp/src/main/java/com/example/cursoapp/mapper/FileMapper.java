package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.File;
import com.example.cursoapp.dto.content.file.FileResponse;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public FileResponse toResponse(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .lectionId(file.getLection().getId())
                .pathToFile(file.getPathToFile())
                .build();
    }
}