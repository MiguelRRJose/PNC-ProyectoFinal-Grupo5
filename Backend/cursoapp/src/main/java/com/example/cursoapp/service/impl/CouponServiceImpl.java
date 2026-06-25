package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.Coupon;
import com.example.cursoapp.domain.entity.Course;
import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.request.CreateCouponRequest;
import com.example.cursoapp.dto.request.UpdateCouponRequest;
import com.example.cursoapp.dto.response.CouponResponse;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.CouponMapper;
import com.example.cursoapp.repository.CouponRepository;
import com.example.cursoapp.repository.CourseRepository;
import com.example.cursoapp.repository.UsuarioRepository;
import com.example.cursoapp.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CourseRepository courseRepository;
    private final UsuarioRepository usuarioRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse createCoupon(CreateCouponRequest request, Long creatorId) {
        if (couponRepository.existsByCode(request.getCode()))
            throw new BusinessRuleException("Coupon code already exists: " + request.getCode());

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Usuario creator = usuarioRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));

        Coupon coupon = Coupon.builder()
                .code(request.getCode())
                .discount(request.getDiscount())
                .course(course)
                .creator(creator)
                .creationDate(LocalDateTime.now())
                .expirationDate(request.getExpirationDate())
                .isActive(true)
                .build();

        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    public List<CouponResponse> getCouponsByCourse(Long courseId) {
        return couponRepository.findByCourseId(courseId)
                .stream().map(couponMapper::toDto).toList();
    }

    @Override
    public CouponResponse getCouponById(Long id) {
        return couponMapper.toDto(couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id)));
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        return couponMapper.toDto(couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code)));
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(Long id, UpdateCouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        if (request.getDiscount() != null) coupon.setDiscount(request.getDiscount());
        if (request.getExpirationDate() != null) coupon.setExpirationDate(request.getExpirationDate());
        if (request.getIsActive() != null) coupon.setIsActive(request.getIsActive());
        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        coupon.setIsActive(false);
        couponRepository.save(coupon);
    }
}