package com.example.cursoapp.dto.commerce.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseRequest {

    @NotNull(message = "Course ID is required.")
    private Long courseId;

    // El cupón es opcional
    private Long couponId;
}
