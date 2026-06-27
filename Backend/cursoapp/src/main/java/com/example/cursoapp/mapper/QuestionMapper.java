package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.Question;
import com.example.cursoapp.dto.content.question.QuestionResponse;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    public QuestionResponse toResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .userId(question.getUser().getId())
                .lectionId(question.getLection().getId())
                .content(question.getContent())
                .creationDate(question.getCreationDate())
                .build();
    }
}