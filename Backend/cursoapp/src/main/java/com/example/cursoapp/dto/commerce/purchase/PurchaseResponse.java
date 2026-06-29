package com.example.cursoapp.dto.commerce.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private Long userId;
    private UUID courseId;  // ← cambió de Long a UUID
    private Double payedAmount;
    private Instant paymentDate;
    private Long couponId;
}