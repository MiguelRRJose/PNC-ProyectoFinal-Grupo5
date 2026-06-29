package com.example.cursoapp.dto.progress.completion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompletionRequest {

    @NotNull(message = "Lection ID is required.")
    private Long lectionId;
}
