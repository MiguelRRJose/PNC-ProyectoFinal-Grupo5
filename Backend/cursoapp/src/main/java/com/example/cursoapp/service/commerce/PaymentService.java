package com.example.cursoapp.service.commerce;

import com.example.cursoapp.dto.commerce.payment.PaymentRequest;
import com.example.cursoapp.dto.commerce.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request, Long userId);
}