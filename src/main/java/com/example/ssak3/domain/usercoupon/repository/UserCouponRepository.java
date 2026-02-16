package com.example.ssak3.domain.usercoupon.repository;

import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserAndCouponAndStatusIn(User user, Coupon coupon, List<UserCouponStatus> statuses);

    @Query("SELECT uc " +
            "FROM UserCoupon uc JOIN uc.coupon c " +
            "WHERE uc.user.id = :userId " +
            "AND c.isDeleted = false " +
            "AND uc.status = :status")
    Page<UserCoupon> findAllActiveCouponsByUserId(Long userId, Pageable pageable, UserCouponStatus status);

    Optional<UserCoupon> findByIdAndUserId(Long userCouponId, Long userId);
}
