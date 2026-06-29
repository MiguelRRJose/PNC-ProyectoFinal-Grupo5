package com.example.cursoapp.service.progress;

import com.example.cursoapp.dto.progress.certified.CertifiedResponse;
import com.example.cursoapp.dto.progress.certified.CreateCertifiedRequest;

import java.util.List;

public interface CertifiedService {
    CertifiedResponse getCertifiedById(Long id);
    List<CertifiedResponse> getCertifiedByUser(Long userId);
    List<CertifiedResponse> getCertifiedByCourse(Long courseId);
    CertifiedResponse createCertified(CreateCertifiedRequest request, Long userId);
}
