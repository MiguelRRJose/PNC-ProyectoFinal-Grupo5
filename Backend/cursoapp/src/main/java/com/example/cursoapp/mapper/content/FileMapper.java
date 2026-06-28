package com.example.cursoapp.mapper.content;

import com.example.cursoapp.domain.entity.content.File;
import com.example.cursoapp.dto.content.file.FileResponse;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileResponse toDto(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .pathToFile(file.getPathToFile())
                .lectionId(file.getLection().getId())
                .build();
    }
}