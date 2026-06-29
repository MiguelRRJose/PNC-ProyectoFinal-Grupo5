package com.example.cursoapp.mapper.progress;

import com.example.cursoapp.domain.entity.progress.Certified;
import com.example.cursoapp.dto.progress.certified.CertifiedResponse;
import org.springframework.stereotype.Component;

@Component
public class CertifiedMapper {

    public CertifiedResponse toDto(Certified certified) {
        return CertifiedResponse.builder()
                .id(certified.getId())
                .userId(certified.getUser().getId())
                .courseId(certified.getCourse().getId())
                .certificationDate(certified.getCertificationDate())
                .format(certified.getFormat())
                .url(certified.getUrl())
                .build();
    }
}
