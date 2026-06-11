package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.File;
import com.example.cursoapp.dto.response.FileResponse;
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