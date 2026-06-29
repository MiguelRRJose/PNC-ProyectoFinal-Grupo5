package com.example.cursoapp.service.commerce.impl;

import com.example.cursoapp.domain.entity.catalogue.Course;
import com.example.cursoapp.domain.entity.commerce.Coupon;
import com.example.cursoapp.domain.entity.commerce.Purchase;
import com.example.cursoapp.domain.entity.identity.Usuario;
import com.example.cursoapp.dto.commerce.purchase.CreatePurchaseRequest;
import com.example.cursoapp.dto.commerce.purchase.PurchaseResponse;
import com.example.cursoapp.exceptions.BusinessRuleException;
import com.example.cursoapp.exceptions.ResourceNotFoundException;
import com.example.cursoapp.mapper.commerce.PurchaseMapper;
import com.example.cursoapp.repository.catalogue.CourseRepository;
import com.example.cursoapp.repository.commerce.CouponRepository;
import com.example.cursoapp.repository.commerce.PurchaseRepository;
import com.example.cursoapp.repository.identity.UsuarioRepository;
import com.example.cursoapp.service.commerce.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final UsuarioRepository usuarioRepository;
    private final CourseRepository courseRepository;
    private final CouponRepository couponRepository;

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {
        return purchaseMapper.toDto(
                purchaseRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getPurchasesByUser(Long userId) {
        return purchaseRepository.findByUserId(userId)
                .stream().map(purchaseMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getPurchasesByCourse(Long courseId) {
        return purchaseRepository.findByCourseId(courseId)
                .stream().map(purchaseMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream().map(purchaseMapper::toDto).toList();
    }

    @Override
    public PurchaseResponse createPurchase(CreatePurchaseRequest request, Long userId) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Course course = courseRepository.findById(UUID.fromString(request.getCourseId().toString()))
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (purchaseRepository.existsByUserIdAndCourseId(userId, request.getCourseId())) {
            throw new BusinessRuleException("User already purchased this course.");
        }

        Coupon coupon = null;
        double finalPrice = course.getPrice();

        if (request.getCouponId() != null) {
            coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + request.getCouponId()));

            if (!coupon.getIsActive()) {
                throw new BusinessRuleException("The coupon is no longer active.");
            }

            finalPrice = finalPrice * (1 - coupon.getDiscount());
        }

        Purchase purchase = Purchase.builder()
                .user(user)
                .course(course)
                .payedAmount(finalPrice)
                .coupon(coupon)
                .build();

        return purchaseMapper.toDto(purchaseRepository.save(purchase));
    }
}
