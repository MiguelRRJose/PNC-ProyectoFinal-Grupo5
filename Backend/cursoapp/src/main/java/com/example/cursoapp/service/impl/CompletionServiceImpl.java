package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.Completion;
import com.example.cursoapp.domain.entity.Lection;
import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.response.CompletionResponse;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.CompletionMapper;
import com.example.cursoapp.repository.CompletionRepository;
import com.example.cursoapp.repository.LectionRepository;
import com.example.cursoapp.repository.UsuarioRepository;
import com.example.cursoapp.service.CompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompletionServiceImpl implements CompletionService {

    private final CompletionRepository completionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LectionRepository lectionRepository;
    private final CompletionMapper completionMapper;

    @Override
    @Transactional
    public CompletionResponse markAsCompleted(Long userId, Long lectionId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Lection lection = lectionRepository.findById(lectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lection not found with id: " + lectionId));

        completionRepository.findByUserIdAndLectionId(userId, lectionId)
                .ifPresent(c -> { throw new BusinessRuleException("Lection already completed by this user."); });

        Completion completion = Completion.builder()
                .user(user)
                .lection(lection)
                .isCompleted(true)
                .build();

        return completionMapper.toDto(completionRepository.save(completion));
    }

    @Override
    public List<CompletionResponse> getCompletionsByUser(Long userId) {
        return completionRepository.findByUserId(userId)
                .stream().map(completionMapper::toDto).toList();
    }

    @Override
    public List<CompletionResponse> getCompletionsByLection(Long lectionId) {
        return completionRepository.findByLectionId(lectionId)
                .stream().map(completionMapper::toDto).toList();
    }
}