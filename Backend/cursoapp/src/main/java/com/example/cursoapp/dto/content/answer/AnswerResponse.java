package com.example.cursoapp.dto.content.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    private Long instructorId;
    private Long questionId;
    private String content;
    private LocalDateTime creationDate;
}
