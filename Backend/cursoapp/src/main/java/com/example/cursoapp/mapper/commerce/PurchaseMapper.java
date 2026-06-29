package com.example.cursoapp.mapper.commerce;

import com.example.cursoapp.domain.entity.commerce.Purchase;
import com.example.cursoapp.dto.commerce.purchase.PurchaseResponse;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    public PurchaseResponse toDto(Purchase purchase) {
        return PurchaseResponse.builder()
                .id(purchase.getId())
                .userId(purchase.getUser().getId())
                .courseId(purchase.getCourse().getId())
                .payedAmount(purchase.getPayedAmount())
                .paymentDate(purchase.getPaymentDate())
                .couponId(purchase.getCoupon() != null ? purchase.getCoupon().getId() : null)
                .build();
    }
}
