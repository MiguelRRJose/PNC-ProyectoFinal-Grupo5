package com.example.cursoapp.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLectionRequest {
    private String title;
    private String content;
}