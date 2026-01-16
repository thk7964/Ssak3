package com.example.ssak3.domain.usercoupon.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponIssueResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponListGetResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponUseResponse;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 추가 로직
     */
    @Transactional
    public UserCouponIssueResponse issueCoupon(Long userId, Long couponId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 이미 쿠폰을 지급 받은 경우
        if (userCouponRepository.existsByUserAndCoupon(user, coupon)) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_EXISTS);
        }

        // 쿠폰 다운로드 수 증가 로직
        coupon.increaseIssuedQuantity();

        // 쿠폰 만료 기한
        // coupon.getValidDays() != null
        // 참(true)인 경우 : 쿠폰 받은 시점 + validDays
        // 거짓(false)인 경우 : IssueEndDate(쿠폰 만료 기한) 기준
        LocalDateTime expiredAt = (coupon.getValidDays() != null) ? LocalDateTime.now().plusDays(coupon.getValidDays()) : coupon.getIssueEndDate();

        UserCoupon userCoupon = new UserCoupon(
                user,
                coupon,
                expiredAt,
                UserCouponStatus.AVAILABLE
        );

        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        return UserCouponIssueResponse.from(savedUserCoupon);

    }

    /**
     * 내 쿠폰 목록 조회 로직
     */
    @Transactional(readOnly = true)
    public PageResponse<UserCouponListGetResponse> getMyCouponList(Long userId, Pageable pageable) {

        Page<UserCouponListGetResponse> userCouponPage = userCouponRepository.findAllByUserId(userId, pageable)
                .map(UserCouponListGetResponse::from);

        return PageResponse.from(userCouponPage);
    }

    /**
     * 쿠폰 사용 처리 로직
     */
    @Transactional
    public UserCouponUseResponse useCoupon(Long userId, Long userCouponId) {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 본인 소유 확인
        if (!userCoupon.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_COUPON_ACCESS);
        }

        // 상태 확인
        if (userCoupon.getStatus() != UserCouponStatus.AVAILABLE) {
            throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
        }

        // 만료일 확인
        if (userCoupon.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.COUPON_EXPIRED);
        }

        // 최소 주문 금액 확인
        // Order 기능 병합 이후 개발 예정


        userCoupon.use();

        return UserCouponUseResponse.from(userCoupon);
    }
}
