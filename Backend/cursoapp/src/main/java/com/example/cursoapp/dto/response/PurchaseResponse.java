package com.example.cursoapp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private Long userId;
    private Long courseId;
    private BigDecimal payedAmount;
    private LocalDateTime paymentDate;
    private Long couponId;
}