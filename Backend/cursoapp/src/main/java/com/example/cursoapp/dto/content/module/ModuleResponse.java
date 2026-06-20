package com.example.cursoapp.dto.content.module;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleResponse {
    private Long id;
    private String title;
    private Integer index;
    private Long courseId;
}