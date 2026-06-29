package com.example.cursoapp.service.commerce.impl;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.commerce.Coupon;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.commerce.coupon.CouponResponse;
import com.example.cursoapp.dto.commerce.coupon.CreateCouponRequest;
import com.example.cursoapp.dto.commerce.coupon.UpdateCouponRequest;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.commerce.CouponMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.commerce.CouponRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.commerce.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final UsuarioRepository usuarioRepository;
    private final CourseRepository courseRepository;

    private Coupon getByIdOrThrow(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCouponById(Long id) {
        return couponMapper.toDto(getByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getCouponByCode(String code) {
        return couponMapper.toDto(
                couponRepository.findByCode(code)
                        .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll()
                .stream().map(couponMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getCouponsByCourse(Long courseId) {
        return couponRepository.findByCourseId(courseId)
                .stream().map(couponMapper::toDto).toList();
    }

    @Override
    public CouponResponse createCoupon(CreateCouponRequest request, Long creatorId) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw new BusinessRuleException("A coupon with code '" + request.getCode() + "' already exists.");
        }

        Usuario creator = usuarioRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));

        Course course = courseRepository.findById(UUID.fromString(request.getCourseId().toString()))
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Coupon coupon = Coupon.builder()
                .code(request.getCode())
                .discount(request.getDiscount())
                .creator(creator)
                .course(course)
                .expirationDate(request.getExpirationDate())
                .build();

        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    public CouponResponse updateCoupon(Long id, UpdateCouponRequest request) {
        Coupon coupon = getByIdOrThrow(id);
        if (request.getDiscount() != null) coupon.setDiscount(request.getDiscount());
        if (request.getExpirationDate() != null) coupon.setExpirationDate(request.getExpirationDate());
        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    public CouponResponse deactivateCoupon(Long id) {
        Coupon coupon = getByIdOrThrow(id);
        coupon.setIsActive(false);
        return couponMapper.toDto(couponRepository.save(coupon));
    }
}
