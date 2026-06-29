package com.example.cursoapp.dto.progress.certified;

import com.example.cursoapp.domain.enums.CertificateFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertifiedResponse {
    private Long id;
    private Long userId;
    private UUID courseId;
    private Instant certificationDate;
    private CertificateFormat format;
    private String url;
}