package com.example.cursoapp.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModuleRequest {
    private String title;
    private Integer index;
}