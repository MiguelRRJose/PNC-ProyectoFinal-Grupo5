package com.example.cursoapp.dto.commerce.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String code;
    private Double discount;
    private Long courseId;
    private Long creatorId;
    private Instant createdAt;
    private Instant expirationDate;
    private Boolean isActive;
}