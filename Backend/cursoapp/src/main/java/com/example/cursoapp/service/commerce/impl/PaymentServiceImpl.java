package com.example.cursoapp.service.commerce.impl;

import com.example.cursoapp.dto.commerce.payment.PaymentRequest;
import com.example.cursoapp.dto.commerce.payment.PaymentResponse;
import com.example.cursoapp.dto.commerce.purchase.CreatePurchaseRequest;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.service.commerce.PaymentService;
import com.example.cursoapp.service.commerce.PurchaseService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PurchaseService purchaseService;

    @Override
    public PaymentResponse processPayment(PaymentRequest request, Long userId) {
        try {
            // Obtener el precio del curso desde purchases (simplificado: precio fijo por ahora)
            // En producción se consultaría el CourseService
            long amountInCents = 100L; // $1.00 en centavos

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .setPaymentMethod(request.getPaymentMethodId())
                    .setConfirm(true)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Crear la compra en BD
                CreatePurchaseRequest purchaseRequest = new CreatePurchaseRequest();
                purchaseRequest.setCourseId(request.getCourseId());
                purchaseService.createPurchase(purchaseRequest, userId);
            }

            return PaymentResponse.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .status(paymentIntent.getStatus())
                    .amount(paymentIntent.getAmount())
                    .currency(paymentIntent.getCurrency())
                    .courseId(request.getCourseId())
                    .userId(userId)
                    .build();

        } catch (StripeException e) {
            throw new RuntimeException("Error procesando el pago: " + e.getMessage());
        }
    }
}