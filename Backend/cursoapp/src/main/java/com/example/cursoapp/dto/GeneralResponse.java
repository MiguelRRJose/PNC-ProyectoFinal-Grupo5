package com.example.cursoapp.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record GeneralResponse (
        String uri,
        String message,
        Integer status,
        Instant time,
        Object data
) {
}