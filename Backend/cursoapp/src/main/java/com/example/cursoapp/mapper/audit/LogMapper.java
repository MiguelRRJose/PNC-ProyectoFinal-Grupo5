package com.example.cursoapp.mapper.audit;

import com.example.cursoapp.domain.entity.audit.Log;
import com.example.cursoapp.dto.audit.LogResponse;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {

    public LogResponse toDto(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .userId(log.getUser() != null ? log.getUser().getId() : null)
                .roleName(log.getRole() != null ? log.getRole().getName().name() : null)
                .entityTypeName(log.getEntityType() != null ? log.getEntityType().getEntityName() : null)
                .actionName(log.getAction().getName().name())
                .entityId(log.getEntityId())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}
