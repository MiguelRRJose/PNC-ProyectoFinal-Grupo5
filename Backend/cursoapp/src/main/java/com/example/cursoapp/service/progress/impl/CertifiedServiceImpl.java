package com.example.cursoapp.service.progress.impl;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.domain.entity.progress.Certified;
import com.example.cursoapp.dto.progress.certified.CertifiedResponse;
import com.example.cursoapp.dto.progress.certified.CreateCertifiedRequest;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.progress.CertifiedMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.repository.progress.CertifiedRepository;
import com.example.cursoapp.service.progress.CertifiedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CertifiedServiceImpl implements CertifiedService {

    private final CertifiedRepository certifiedRepository;
    private final CertifiedMapper certifiedMapper;
    private final UsuarioRepository usuarioRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public CertifiedResponse getCertifiedById(Long id) {
        return certifiedMapper.toDto(
                certifiedRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertifiedResponse> getCertifiedByUser(Long userId) {
        return certifiedRepository.findByUserId(userId)
                .stream().map(certifiedMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertifiedResponse> getCertifiedByCourse(Long courseId) {
        return certifiedRepository.findByCourseId(courseId)
                .stream().map(certifiedMapper::toDto).toList();
    }

    @Override
    public CertifiedResponse createCertified(CreateCertifiedRequest request, Long userId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Course course = courseRepository.findById(UUID.fromString(request.getCourseId().toString()))
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (certifiedRepository.existsByUserIdAndCourseId(userId, request.getCourseId())) {
            throw new BusinessRuleException("User already has a certificate for this course.");
        }

        Certified certified = Certified.builder()
                .user(user)
                .course(course)
                .format(request.getFormat())
                .url(request.getUrl())
                .build();

        return certifiedMapper.toDto(certifiedRepository.save(certified));
    }
}
