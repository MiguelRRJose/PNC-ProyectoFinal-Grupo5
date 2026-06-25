package com.example.cursoapp.mapper;

import com.example.cursoapp.domain.entity.Coupon;
import com.example.cursoapp.dto.response.CouponResponse;
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
                .creationDate(coupon.getCreationDate())
                .expirationDate(coupon.getExpirationDate())
                .isActive(coupon.getIsActive())
                .build();
    }
}