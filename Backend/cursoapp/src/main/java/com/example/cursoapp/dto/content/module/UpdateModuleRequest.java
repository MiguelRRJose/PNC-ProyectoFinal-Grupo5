package com.example.cursoapp.dto.content.module;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModuleRequest {
    private String title;
    private Integer index;
}