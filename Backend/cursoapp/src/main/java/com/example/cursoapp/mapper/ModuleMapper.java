package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.Module;
import com.example.cursoapp.dto.request.CreateModuleRequest;
import com.example.cursoapp.dto.response.ModuleResponse;
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