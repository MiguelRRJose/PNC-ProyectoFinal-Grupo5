package com.example.cursoapp.dto.commerce.coupon;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCouponRequest {

    @DecimalMin(value = "0.0", message = "Discount cannot be negative.")
    @DecimalMax(value = "1.0", message = "Discount cannot exceed 100%.")
    private Double discount;

    private Instant expirationDate;
}
