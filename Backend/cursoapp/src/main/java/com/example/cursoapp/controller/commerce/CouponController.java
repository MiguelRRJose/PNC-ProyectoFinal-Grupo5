package com.example.cursoapp.controller.commerce;

import com.example.cursoapp.dto.GeneralResponse;
import com.example.cursoapp.dto.commerce.coupon.CreateCouponRequest;
import com.example.cursoapp.dto.commerce.coupon.UpdateCouponRequest;
import com.example.cursoapp.service.commerce.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.cursoapp.config.UsuarioDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    private ResponseEntity<GeneralResponse> buildResponse(Object data, String message, HttpStatus status) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(Instant.now())
                        .data(data)
                        .build()
                );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        return buildResponse(
                couponService.getAllCoupons(),
                "Coupons successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable Long id) {
        return buildResponse(
                couponService.getCouponById(id),
                "Coupon successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-code")
    public ResponseEntity<GeneralResponse> getByCode(@RequestParam String code) {
        return buildResponse(
                couponService.getCouponByCode(code),
                "Coupon successfully found.",
                HttpStatus.OK
        );
    }

    @GetMapping("/by-course")
    public ResponseEntity<GeneralResponse> getByCourse(@RequestParam Long courseId) {
        return buildResponse(
                couponService.getCouponsByCourse(courseId),
                "Coupons successfully found.",
                HttpStatus.OK
        );
    }

    // Solo INSTRUCTOR o ADMIN deberían poder crear cupones
    @PostMapping
    public ResponseEntity<GeneralResponse> createCoupon(@RequestBody @Valid CreateCouponRequest request) {
        UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long creatorId = usuarioDetails.getId();
        return buildResponse(
                couponService.createCoupon(request, creatorId),
                "Coupon successfully created.",
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateCoupon(@PathVariable Long id,
                                                         @RequestBody @Valid UpdateCouponRequest request) {
        return buildResponse(
                couponService.updateCoupon(id, request),
                "Coupon successfully updated.",
                HttpStatus.OK
        );
    }

    // Soft delete — el cupón queda en BD para mantener historial de compras
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<GeneralResponse> deactivateCoupon(@PathVariable Long id) {
        return buildResponse(
                couponService.deactivateCoupon(id),
                "Coupon successfully deactivated.",
                HttpStatus.OK
        );
    }
}
