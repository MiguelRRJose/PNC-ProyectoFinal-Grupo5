package com.example.cursoapp.service.progress.impl;

import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.domain.entity.progress.Completion;
import com.example.cursoapp.dto.progress.completion.CompletionResponse;
import com.example.cursoapp.dto.progress.completion.CreateCompletionRequest;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.progress.CompletionMapper;
import com.example.cursoapp.repository.content.LectionRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.repository.progress.CompletionRepository;
import com.example.cursoapp.service.progress.CompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompletionServiceImpl implements CompletionService {

    private final CompletionRepository completionRepository;
    private final CompletionMapper completionMapper;
    private final UsuarioRepository usuarioRepository;
    private final LectionRepository lectionRepository;

    @Override
    @Transactional(readOnly = true)
    public CompletionResponse getCompletionById(Long id) {
        return completionMapper.toDto(
                completionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Completion not found with id: " + id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompletionResponse> getCompletionsByUser(Long userId) {
        return completionRepository.findByUserId(userId)
                .stream().map(completionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompletionResponse> getCompletedByUser(Long userId) {
        return completionRepository.findByUserIdAndIsCompleted(userId, true)
                .stream().map(completionMapper::toDto).toList();
    }

    @Override
    public CompletionResponse markAsCompleted(CreateCompletionRequest request, Long userId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Lection lection = lectionRepository.findById(request.getLectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + request.getLectionId()));

        // Si ya existe el registro, solo actualizamos
        Completion completion = completionRepository
                .findByUserIdAndLectionId(userId, request.getLectionId())
                .orElse(Completion.builder().user(user).lection(lection).build());

        if (completion.getIsCompleted()) {
            throw new BusinessRuleException("Lection is already marked as completed.");
        }

        completion.setIsCompleted(true);
        return completionMapper.toDto(completionRepository.save(completion));
    }

    @Override
    public CompletionResponse markAsIncomplete(Long userId, Long lectionId) {
        Completion completion = completionRepository.findByUserIdAndLectionId(userId, lectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Completion record not found for user and lection."));

        completion.setIsCompleted(false);
        return completionMapper.toDto(completionRepository.save(completion));
    }
}
