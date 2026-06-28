package com.example.cursoapp.service.content;

import com.example.cursoapp.dto.content.module.CreateModuleRequest;
import com.example.cursoapp.dto.content.module.UpdateModuleRequest;
import com.example.cursoapp.dto.content.module.ModuleResponse;

import java.util.List;

public interface ModuleService {
    ModuleResponse createModule(CreateModuleRequest request);
    List<ModuleResponse> getModulesByCourse(Long courseId);
    ModuleResponse getModuleById(Long id);
    ModuleResponse updateModule(Long id, UpdateModuleRequest request);
    void deleteModule(Long id);
}