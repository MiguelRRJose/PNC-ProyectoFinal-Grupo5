package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.Question;
import com.example.cursoapp.dto.content.question.QuestionResponse;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionResponse toDto(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .content(question.getContent())
                .lectionId(question.getLection().getId())
                .userId(question.getUser().getId())
                .creationDate(question.getCreationDate())
                .build();
    }
}