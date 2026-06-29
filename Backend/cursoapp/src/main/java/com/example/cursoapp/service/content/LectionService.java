package com.example.cursoapp.service.content;

import com.example.cursoapp.dto.content.lection.CreateLectionRequest;
import com.example.cursoapp.dto.content.lection.LectionResponse;
import com.example.cursoapp.dto.content.lection.UpdateLectionRequest;

import java.util.List;

public interface LectionService {
    LectionResponse createLection(CreateLectionRequest request);
    List<LectionResponse> getLectionsByModule(Long moduleId);
    LectionResponse getLectionById(Long id);
    LectionResponse updateLection(Long id, UpdateLectionRequest request);
    void deleteLection(Long id);
}