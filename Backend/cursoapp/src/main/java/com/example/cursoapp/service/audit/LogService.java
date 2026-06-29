package com.example.cursoapp.service.audit;

import com.example.cursoapp.domain.enums.ActionType;
import com.example.cursoapp.dto.audit.LogResponse;

import java.util.List;

public interface LogService {
    LogResponse getLogById(Long id);
    List<LogResponse> getAllLogs();
    List<LogResponse> getLogsByUser(Long userId);
    List<LogResponse> getLogsByEntity(Long entityTypeId);
    List<LogResponse> getLogsByEntityId(Long entityId);

    // Método utilitario para registrar acciones desde otros servicios
    void registerLog(Long userId, Long roleId, String entityTypeName, ActionType actionType, Long entityId, String details);
}
