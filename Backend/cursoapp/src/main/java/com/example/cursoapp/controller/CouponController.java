package com.example.cursoapp.controller;

import com.example.cursoapp.dto.request.CreateCouponRequest;
import com.example.cursoapp.dto.request.UpdateCouponRequest;
import com.example.cursoapp.dto.response.CouponResponse;
import com.example.cursoapp.dto.response.GeneralResponse;
import com.example.cursoapp.service.impl.CouponServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponServiceImpl couponService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                GeneralResponse.builder()
                        .data(data)
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @PostMapping("/creator/{creatorId}")
    public ResponseEntity<GeneralResponse> createCoupon(
            @Valid @RequestBody CreateCouponRequest request,
            @PathVariable Long creatorId,
            HttpServletRequest httpRequest) {
        CouponResponse response = couponService.createCoupon(request, creatorId);
        return buildResponse(response, "Coupon created successfully.", HttpStatus.CREATED, httpRequest);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<GeneralResponse> getCouponsByCourse(
            @PathVariable Long courseId,
            HttpServletRequest httpRequest) {
        List<CouponResponse> response = couponService.getCouponsByCourse(courseId);
        return buildResponse(response, "Coupons retrieved successfully.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getCouponById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        CouponResponse response = couponService.getCouponById(id);
        return buildResponse(response, "Coupon found.", HttpStatus.OK, httpRequest);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<GeneralResponse> getCouponByCode(
            @PathVariable String code,
            HttpServletRequest httpRequest) {
        CouponResponse response = couponService.getCouponByCode(code);
        return buildResponse(response, "Coupon found.", HttpStatus.OK, httpRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCouponRequest request,
            HttpServletRequest httpRequest) {
        CouponResponse response = couponService.updateCoupon(id, request);
        return buildResponse(response, "Coupon updated successfully.", HttpStatus.OK, httpRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteCoupon(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        couponService.deleteCoupon(id);
        return buildResponse(null, "Coupon deactivated successfully.", HttpStatus.OK, httpRequest);
    }
}