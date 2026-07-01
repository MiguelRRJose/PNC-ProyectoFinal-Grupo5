package com.example.cursoapp.dto.commerce.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull
    private Long courseId;

    @NotNull
    private String paymentMethodId;
}