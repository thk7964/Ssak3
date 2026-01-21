package com.example.ssak3.domain.usercoupon.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponDeleteResponse;
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
import java.util.List;

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

        // 재발급을 막아야 하는 상태 리스트
        // AVAILABLE(사용 가능), USED(사용 완료), USED_DELETED(사용 후 삭제)
        List<UserCouponStatus> restrictedStatuses = List.of(
                UserCouponStatus.AVAILABLE,
                UserCouponStatus.USED,
                UserCouponStatus.USED_DELETED
        );


        // 관리자는 쿠폰을 발급받을 수 없음
        if (user.getRole() == UserRole.ADMIN) {
            throw new CustomException(ErrorCode.ADMIN_CANNOT_ISSUE_COUPON);
        }

        // 쿠폰 재발급 차단
        if (userCouponRepository.existsByUserAndCouponAndStatusIn(user, coupon, restrictedStatuses)) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_EXISTS);
        }

        // 삭제된 쿠폰인지 확인
        if (coupon.isDeleted()) {
            throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
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
    public PageResponse<UserCouponListGetResponse> getMyCouponList(Long userId, Pageable pageable, UserCouponStatus status) {

        Page<UserCouponListGetResponse> userCouponPage = userCouponRepository.findAllActiveCouponsByUserId(userId, pageable, status)
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

    /**
     * 내 쿠폰 삭제
     */
    @Transactional
    public UserCouponDeleteResponse deleteUserCoupon(Long userId, Long userCouponId) {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 본인 쿠폰인지 확인
        if (!userCoupon.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_COUPON_ACCESS);
        }

        // 이미 삭제된 쿠폰인지 확인
        if (userCoupon.getStatus() == UserCouponStatus.DELETED || userCoupon.getStatus() == UserCouponStatus.USED_DELETED) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_DELETED);
        }

        // if) 미사용 삭제 : 수량 복구 + DELETED 상태로 변경
        // else if)  사용후 삭제: 수량 복구 X + USED_DELETED 상태로 변경
        if (userCoupon.getStatus() == UserCouponStatus.AVAILABLE) {
            userCoupon.getCoupon().decreaseIssuedQuantity();
            userCoupon.changeStatus(UserCouponStatus.DELETED);
        } else if (userCoupon.getStatus() == UserCouponStatus.USED) {
            userCoupon.changeStatus(UserCouponStatus.USED_DELETED);
        }

        return UserCouponDeleteResponse.from(userCoupon);
    }
}
