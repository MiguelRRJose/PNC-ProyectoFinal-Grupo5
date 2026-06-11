package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreateModuleRequest;
import com.example.cursoapp.dto.request.UpdateModuleRequest;
import com.example.cursoapp.dto.response.ModuleResponse;

import java.util.List;

public interface ModuleService {
    ModuleResponse createModule(CreateModuleRequest request);
    List<ModuleResponse> getModulesByCourse(Long courseId);
    ModuleResponse getModuleById(Long id);
    ModuleResponse updateModule(Long id, UpdateModuleRequest request);
    void deleteModule(Long id);
}