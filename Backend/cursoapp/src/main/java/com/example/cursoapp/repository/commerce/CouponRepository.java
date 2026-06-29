package com.example.cursoapp.repository.commerce;

import com.example.cursoapp.domain.entity.commerce.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findByCourseId(Long courseId);
    List<Coupon> findByCreatorId(Long creatorId);
    List<Coupon> findByIsActive(Boolean isActive);
    boolean existsByCode(String code);
}
