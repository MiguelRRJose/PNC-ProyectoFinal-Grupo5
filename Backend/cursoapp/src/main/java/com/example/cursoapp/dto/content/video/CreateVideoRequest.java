package com.example.cursoapp.dto.content.video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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