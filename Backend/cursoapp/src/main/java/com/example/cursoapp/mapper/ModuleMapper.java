package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.Module;
import com.example.cursoapp.dto.content.module.ModuleResponse;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {
    public ModuleResponse toResponse(Module module) {
        return ModuleResponse.builder()
                .id(module.getId())
                .courseId(module.getCourse().getId())
                .title(module.getTitle())
                .index(module.getIndex())
                .build();
    }
}