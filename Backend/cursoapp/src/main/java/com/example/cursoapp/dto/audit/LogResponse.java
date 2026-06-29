package com.example.cursoapp.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
    private Long id;
    private Long userId;
    private String roleName;
    private String entityTypeName;
    private String actionName;
    private Long entityId;
    private String details;
    private Instant timestamp;
}
