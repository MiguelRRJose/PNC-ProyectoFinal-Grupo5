package com.example.cursoapp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String content;
    private Long lectionId;
    private Long userId;
    private LocalDateTime creationDate;
}