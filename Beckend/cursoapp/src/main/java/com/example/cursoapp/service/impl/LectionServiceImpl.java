package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.Lection;
import com.example.cursoapp.domain.entity.Module;
import com.example.cursoapp.dto.request.CreateLectionRequest;
import com.example.cursoapp.dto.request.UpdateLectionRequest;
import com.example.cursoapp.dto.response.LectionResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.LectionMapper;
import com.example.cursoapp.repository.LectionRepository;
import com.example.cursoapp.repository.ModuleRepository;
import com.example.cursoapp.service.LectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectionServiceImpl implements LectionService {

    private final LectionRepository lectionRepository;
    private final ModuleRepository moduleRepository;
    private final LectionMapper lectionMapper;

    @Override
    @Transactional
    public LectionResponse createLection(CreateLectionRequest request) {
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + request.getModuleId()));
        Lection lection = Lection.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .module(module)
                .build();
        return lectionMapper.toDto(lectionRepository.save(lection));
    }

    @Override
    public List<LectionResponse> getLectionsByModule(Long moduleId) {
        return lectionRepository.findByModuleId(moduleId)
                .stream().map(lectionMapper::toDto).toList();
    }

    @Override
    public LectionResponse getLectionById(Long id) {
        return lectionMapper.toDto(lectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + id)));
    }

    @Override
    @Transactional
    public LectionResponse updateLection(Long id, UpdateLectionRequest request) {
        Lection lection = lectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + id));
        if (request.getTitle() != null) lection.setTitle(request.getTitle());
        if (request.getContent() != null) lection.setContent(request.getContent());
        return lectionMapper.toDto(lectionRepository.save(lection));
    }

    @Override
    @Transactional
    public void deleteLection(Long id) {
        lectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + id));
        lectionRepository.deleteById(id);
    }
}