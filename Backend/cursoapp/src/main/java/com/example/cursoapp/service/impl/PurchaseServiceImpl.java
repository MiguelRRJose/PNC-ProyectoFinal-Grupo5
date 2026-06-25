package com.example.cursoapp.service.impl;

import com.example.cursoapp.domain.entity.Coupon;
import com.example.cursoapp.domain.entity.Course;
import com.example.cursoapp.domain.entity.Purchase;
import com.example.cursoapp.domain.entity.Usuario;
import com.example.cursoapp.dto.request.CreatePurchaseRequest;
import com.example.cursoapp.dto.response.PurchaseResponse;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.PurchaseMapper;
import com.example.cursoapp.repository.CouponRepository;
import com.example.cursoapp.repository.CourseRepository;
import com.example.cursoapp.repository.PurchaseRepository;
import com.example.cursoapp.repository.UsuarioRepository;
import com.example.cursoapp.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UsuarioRepository usuarioRepository;
    private final CourseRepository courseRepository;
    private final CouponRepository couponRepository;
    private final PurchaseMapper purchaseMapper;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(CreatePurchaseRequest request, Long userId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (purchaseRepository.existsByUserIdAndCourseId(userId, request.getCourseId()))
            throw new BusinessRuleException("User already purchased this course.");

        Coupon coupon = null;
        if (request.getCouponCode() != null) {
            coupon = couponRepository.findByCode(request.getCouponCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + request.getCouponCode()));
            if (!coupon.getIsActive())
                throw new BusinessRuleException("Coupon is no longer active.");
        }

        Purchase purchase = Purchase.builder()
                .user(user)
                .course(course)
                .coupon(coupon)
                .payedAmount(course.getPrice())
                .paymentDate(LocalDateTime.now())
                .build();

        return purchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    @Override
    public List<PurchaseResponse> getPurchasesByUser(Long userId) {
        return purchaseRepository.findByUserId(userId)
                .stream().map(purchaseMapper::toDto).toList();
    }

    @Override
    public List<PurchaseResponse> getPurchasesByCourse(Long courseId) {
        return purchaseRepository.findByCourseId(courseId)
                .stream().map(purchaseMapper::toDto).toList();
    }

    @Override
    public PurchaseResponse getPurchaseById(Long id) {
        return purchaseMapper.toDto(purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id)));
    }
}