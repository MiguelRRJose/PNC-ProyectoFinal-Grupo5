package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.content.Answer;
import com.example.cursoapp.dto.content.answer.AnswerResponse;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public AnswerResponse toResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .instructorId(answer.getInstructor().getId())
                .questionId(answer.getQuestion().getId())
                .content(answer.getContent())
                .creationDate(answer.getCreationDate())
                .build();
    }
}