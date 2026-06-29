package com.example.cursoapp.mapper.content;

import com.example.cursoapp.domain.entity.content.Module;
import com.example.cursoapp.dto.content.module.ModuleResponse;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public ModuleResponse toDto(Module module) {
        return ModuleResponse.builder()
                .id(module.getId())
                .title(module.getTitle())
                .index(module.getIndex())
                .courseId(module.getCourse().getId())
                .build();
    }
}