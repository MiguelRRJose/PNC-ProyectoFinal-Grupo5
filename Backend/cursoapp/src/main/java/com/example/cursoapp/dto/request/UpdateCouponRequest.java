package com.example.cursoapp.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCouponRequest {
    private BigDecimal discount;
    private LocalDateTime expirationDate;
    private Boolean isActive;
}