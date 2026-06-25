package com.example.cursoapp.service;

import com.example.cursoapp.dto.request.CreateCouponRequest;
import com.example.cursoapp.dto.request.UpdateCouponRequest;
import com.example.cursoapp.dto.response.CouponResponse;

import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CreateCouponRequest request, Long creatorId);
    List<CouponResponse> getCouponsByCourse(Long courseId);
    CouponResponse getCouponById(Long id);
    CouponResponse getCouponByCode(String code);
    CouponResponse updateCoupon(Long id, UpdateCouponRequest request);
    void deleteCoupon(Long id);
}