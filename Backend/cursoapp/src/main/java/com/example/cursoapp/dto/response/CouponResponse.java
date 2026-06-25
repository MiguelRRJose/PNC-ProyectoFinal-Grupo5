package com.example.cursoapp.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String code;
    private BigDecimal discount;
    private Long courseId;
    private Long creatorId;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private Boolean isActive;
}