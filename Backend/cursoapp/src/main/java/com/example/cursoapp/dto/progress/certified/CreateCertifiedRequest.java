package com.example.cursoapp.dto.progress.certified;

import com.example.cursoapp.domain.enums.CertificateFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertifiedRequest {

    @NotNull(message = "Course ID is required.")
    private Long courseId;

    @NotNull(message = "Certificate format is required.")
    private CertificateFormat format;

    private String url;
}
