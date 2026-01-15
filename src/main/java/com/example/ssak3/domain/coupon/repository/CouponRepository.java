package com.example.ssak3.domain.coupon.repository;

import com.example.ssak3.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 삭제되지 않은 쿠폰 목록 조회
    List<Coupon> findAllByIsDeletedFalse();

    // 특정 ID의 쿠폰 중 삭제되지 않은 쿠폰만 조회
    Optional<Coupon> findByIdAndIsDeletedFalse(Long id);

}
