package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.dto.content.lection.LectionResponse;
import org.springframework.stereotype.Component;

@Component
public class LectionMapper {
    public LectionResponse toResponse(Lection lection) {
        return LectionResponse.builder()
                .id(lection.getId())
                .moduleId(lection.getModule().getId())
                .title(lection.getTitle())
                .content(lection.getContent())
                .build();
    }
}