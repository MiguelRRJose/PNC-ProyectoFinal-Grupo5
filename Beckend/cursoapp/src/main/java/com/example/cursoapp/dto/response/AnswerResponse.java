package com.example.cursoapp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    private String content;
    private Long questionId;
    private Long instructorId;
    private LocalDateTime creationDate;
}