package com.example.cursoapp.dto.content.video;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVideoRequest {

    @NotBlank(message = "Video URL is required.")
    private String videoUrl;

    @NotNull(message = "Lection ID is required.")
    private Long lectionId;
}