package com.example.cursoapp.service.content.impl;

import com.example.cursoapp.domain.entity.Course;
import com.example.cursoapp.domain.entity.content.Module;
import com.example.cursoapp.dto.content.module.CreateModuleRequest;
import com.example.cursoapp.dto.content.module.UpdateModuleRequest;
import com.example.cursoapp.dto.content.module.ModuleResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.content.ModuleMapper;
import com.example.cursoapp.repository.CourseRepository;
import com.example.cursoapp.repository.content.ModuleRepository;
import com.example.cursoapp.service.content.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModuleMapper moduleMapper;

    @Override
    @Transactional
    public ModuleResponse createModule(CreateModuleRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
        Module module = Module.builder()
                .title(request.getTitle())
                .index(request.getIndex())
                .course(course)
                .build();
        return moduleMapper.toDto(moduleRepository.save(module));
    }

    @Override
    public List<ModuleResponse> getModulesByCourse(Long courseId) {
        return moduleRepository.findByCourseId(courseId)
                .stream().map(moduleMapper::toDto).toList();
    }

    @Override
    public ModuleResponse getModuleById(Long id) {
        return moduleMapper.toDto(moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id)));
    }

    @Override
    @Transactional
    public ModuleResponse updateModule(Long id, UpdateModuleRequest request) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        if (request.getTitle() != null) module.setTitle(request.getTitle());
        if (request.getIndex() != null) module.setIndex(request.getIndex());
        return moduleMapper.toDto(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        moduleRepository.deleteById(id);
    }
}