package com.example.cursoapp.mapper.content;

import com.example.cursoapp.domain.entity.content.Lection;
import com.example.cursoapp.dto.content.lection.LectionResponse;
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