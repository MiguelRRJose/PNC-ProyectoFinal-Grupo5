package com.example.cursoapp.dto.content.lection;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLectionRequest {
    private String title;
    private String content;
}