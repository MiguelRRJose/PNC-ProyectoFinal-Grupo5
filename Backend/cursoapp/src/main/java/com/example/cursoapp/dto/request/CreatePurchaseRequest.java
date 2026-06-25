package com.example.cursoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseRequest {

    @NotNull(message = "Course ID is required.")
    private Long courseId;

    private String couponCode;
}