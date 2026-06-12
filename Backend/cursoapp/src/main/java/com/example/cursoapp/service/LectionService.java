package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreateLectionRequest;
import com.example.cursoapp.dto.request.UpdateLectionRequest;
import com.example.cursoapp.dto.response.LectionResponse;

import java.util.List;

public interface LectionService {
    LectionResponse createLection(CreateLectionRequest request);
    List<LectionResponse> getLectionsByModule(Long moduleId);
    LectionResponse getLectionById(Long id);
    LectionResponse updateLection(Long id, UpdateLectionRequest request);
    void deleteLection(Long id);
}