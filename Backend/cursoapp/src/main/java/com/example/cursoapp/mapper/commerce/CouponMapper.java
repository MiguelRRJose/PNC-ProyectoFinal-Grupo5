package com.example.cursoapp.mapper.commerce;

import com.example.cursoapp.domain.entity.commerce.Coupon;
import com.example.cursoapp.dto.commerce.coupon.CouponResponse;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponResponse toDto(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discount(coupon.getDiscount())
                .courseId(coupon.getCourse().getId())
                .creatorId(coupon.getCreator().getId())
                .createdAt(coupon.getCreatedAt())
                .expirationDate(coupon.getExpirationDate())
                .isActive(coupon.getIsActive())
                .build();
    }
}
