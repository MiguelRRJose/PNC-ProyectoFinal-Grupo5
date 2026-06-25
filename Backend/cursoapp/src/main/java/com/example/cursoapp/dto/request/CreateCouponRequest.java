package com.example.cursoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCouponRequest {

    @NotBlank(message = "Code is required.")
    private String code;

    @NotNull(message = "Discount is required.")
    @DecimalMin(value = "0.01", message = "Discount must be greater than 0.")
    private BigDecimal discount;

    @NotNull(message = "Course ID is required.")
    private Long courseId;

    @NotNull(message = "Expiration date is required.")
    private LocalDateTime expirationDate;
}