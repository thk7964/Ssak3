package com.example.ssak3.domain.coupon.repository;

import com.example.ssak3.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 사용 가능한 쿠폰만 조회
    @Query("SELECT c " +
            "FROM Coupon c " +
            "WHERE c.issueEndDate >= :now " +   // 쿠폰 발행 일자가 현재보다 큰 쿠폰 (즉, 만료되지 않은 쿠폰)
            "AND c.isDeleted = false")          // 삭제 되지 않은 쿠폰
    Page<Coupon> findAllAvailableCoupons(LocalDateTime now, Pageable pageable);
}
