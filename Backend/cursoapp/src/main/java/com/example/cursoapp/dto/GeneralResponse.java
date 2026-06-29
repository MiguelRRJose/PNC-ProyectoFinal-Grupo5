package com.example.cursoapp.dto;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record GeneralResponse (
        String uri,
        String message,
        Integer status,
        Instant time,
        Object data
) {
}