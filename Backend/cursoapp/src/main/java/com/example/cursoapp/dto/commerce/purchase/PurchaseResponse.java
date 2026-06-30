package com.example.cursoapp.dto.commerce.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private Long userId;
    private Long courseId;
    private Double payedAmount;
    private Instant paymentDate;
    private Long couponId;
}