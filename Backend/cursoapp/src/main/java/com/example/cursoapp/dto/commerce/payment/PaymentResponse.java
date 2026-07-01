package com.example.cursoapp.dto.commerce.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String paymentIntentId;
    private String status;
    private Long amount;
    private String currency;
    private Long courseId;
    private Long userId;
}