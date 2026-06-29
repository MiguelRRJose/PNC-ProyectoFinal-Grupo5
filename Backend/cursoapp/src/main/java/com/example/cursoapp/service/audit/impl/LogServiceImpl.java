package com.example.cursoapp.service.audit.impl;

import com.example.cursoapp.domain.entity.audit.Action;
import com.example.cursoapp.domain.enums.ActionType;
import com.example.cursoapp.domain.entity.audit.EntityType;
import com.example.cursoapp.domain.entity.audit.Log;
import com.example.cursoapp.domain.entity.identity.Role;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.audit.LogResponse;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.audit.LogMapper;
import com.example.cursoapp.repository.audit.ActionRepository;
import com.example.cursoapp.repository.audit.EntityTypeRepository;
import com.example.cursoapp.repository.audit.LogRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.audit.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final LogMapper logMapper;
    private final ActionRepository actionRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public LogResponse getLogById(Long id) {
        return logMapper.toDto(
                logRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Log not found with id: " + id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogResponse> getAllLogs() {
        return logRepository.findAll()
                .stream().map(logMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogResponse> getLogsByUser(Long userId) {
        return logRepository.findByUserId(userId)
                .stream().map(logMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogResponse> getLogsByEntity(Long entityTypeId) {
        return logRepository.findByEntityTypeId(entityTypeId)
                .stream().map(logMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogResponse> getLogsByEntityId(Long entityId) {
        return logRepository.findByEntityId(entityId)
                .stream().map(logMapper::toDto).toList();
    }

    @Override
    public void registerLog(Long userId, Long roleId, String entityTypeName,
                            ActionType actionType, Long entityId, String details) {
        Action action = actionRepository.findByName(actionType)
                .orElseThrow(() -> new ResourceNotFoundException("Action not found: " + actionType));

        // Usuario y role pueden ser null (ej: login fallido)
        Usuario user = userId != null
                ? usuarioRepository.findById(userId).orElse(null)
                : null;

        Role role = user != null ? user.getRole() : null;

        // EntityType puede ser null (ej: login, logout)
        EntityType entityType = entityTypeName != null
                ? entityTypeRepository.findByEntityName(entityTypeName).orElse(null)
                : null;

        Log log = Log.builder()
                .user(user)
                .role(role)
                .entityType(entityType)
                .action(action)
                .entityId(entityId)
                .details(details)
                .build();

        logRepository.save(log);
    }
}
