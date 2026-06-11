package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.Lection;
import com.example.cursoapp.dto.response.LectionResponse;
import org.springframework.stereotype.Component;

@Component
public class LectionMapper {

    public LectionResponse toDto(Lection lection) {
        return LectionResponse.builder()
                .id(lection.getId())
                .title(lection.getTitle())
                .content(lection.getContent())
                .moduleId(lection.getModule().getId())
                .build();
    }
}