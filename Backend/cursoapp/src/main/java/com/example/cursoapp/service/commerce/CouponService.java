package com.example.cursoapp.service.commerce;

import com.example.cursoapp.dto.commerce.coupon.CouponResponse;
import com.example.cursoapp.dto.commerce.coupon.CreateCouponRequest;
import com.example.cursoapp.dto.commerce.coupon.UpdateCouponRequest;

import java.util.List;

public interface CouponService {
    CouponResponse getCouponById(Long id);
    CouponResponse getCouponByCode(String code);
    List<CouponResponse> getAllCoupons();
    List<CouponResponse> getCouponsByCourse(Long courseId);
    CouponResponse createCoupon(CreateCouponRequest request, Long creatorId);
    CouponResponse updateCoupon(Long id, UpdateCouponRequest request);
    CouponResponse deactivateCoupon(Long id);
}
