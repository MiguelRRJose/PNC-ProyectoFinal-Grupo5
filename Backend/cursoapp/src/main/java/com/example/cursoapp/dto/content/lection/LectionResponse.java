package com.example.cursoapp.dto.content.lection;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectionResponse {
    private Long id;
    private String title;
    private String content;
    private Long moduleId;
}