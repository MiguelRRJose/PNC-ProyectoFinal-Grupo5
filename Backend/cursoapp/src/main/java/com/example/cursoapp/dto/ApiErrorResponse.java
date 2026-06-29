package com.example.cursoapp.dto;

import lombok.Builder;
import java.time.Instant;

@Builder
public record ApiErrorResponse (
    String message,
    Integer code,
    Instant timestamp,
    Object errors
) {
}