package com.example.cursoapp.dto.content.video;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String videoUrl;
    private Long lectionId;
}