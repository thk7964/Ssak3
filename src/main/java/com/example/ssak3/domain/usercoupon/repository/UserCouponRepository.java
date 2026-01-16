package com.example.ssak3.domain.usercoupon.repository;

import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    // 헤딩 유저가 이미 해당 쿠폰을 받았는지 확인
    boolean existsByUserAndCoupon(User user, Coupon coupon);

    // 내 쿠폰 목록 페이징 조회
    Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable);
}
