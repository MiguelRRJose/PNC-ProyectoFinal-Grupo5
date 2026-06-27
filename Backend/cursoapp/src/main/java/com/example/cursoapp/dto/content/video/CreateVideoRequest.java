package com.example.cursoapp.dto.content.video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateVideoRequest {
    @NotNull(message = "lection_id is required")
    private Long lectionId;

    @NotBlank(message = "video_url is required")
    private String videoUrl;
}