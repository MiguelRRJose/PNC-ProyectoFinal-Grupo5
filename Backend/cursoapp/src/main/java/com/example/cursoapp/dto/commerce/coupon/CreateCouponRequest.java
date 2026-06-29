package com.example.cursoapp.dto.commerce.coupon;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCouponRequest {

    @NotBlank(message = "Coupon code is required.")
    private String code;

    @NotNull(message = "Discount is required.")
    @DecimalMin(value = "0.0", message = "Discount cannot be negative.")
    @DecimalMax(value = "1.0", message = "Discount cannot exceed 100%.")
    private Double discount;

    @NotNull(message = "Course ID is required.")
    private Long courseId;

    @NotNull(message = "Expiration date is required.")
    private Instant expirationDate;
}
