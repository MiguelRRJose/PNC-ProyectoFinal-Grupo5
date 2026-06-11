package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.Answer;
import com.example.cursoapp.dto.response.AnswerResponse;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public AnswerResponse toDto(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .questionId(answer.getQuestion().getId())
                .instructorId(answer.getInstructor().getId())
                .creationDate(answer.getCreationDate())
                .build();
    }
}