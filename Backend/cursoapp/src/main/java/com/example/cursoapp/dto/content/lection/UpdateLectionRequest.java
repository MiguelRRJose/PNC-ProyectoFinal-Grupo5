package com.example.cursoapp.dto.content.lection;

import lombok.Data;

@Data
public class UpdateLectionRequest {
    private String title;
    private String content;
}