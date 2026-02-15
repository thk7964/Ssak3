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
import com.example.ssak3.domain.usercoupon.model.response.CouponListForUserGetResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponDeleteResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponIssueResponse;
import com.example.ssak3.domain.usercoupon.model.response.UserCouponListGetResponse;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponCacheService userCouponCacheService;

    /**
     * 쿠폰 목록 조회 로직 (사용자용)
     */
    @Transactional(readOnly = true)
    public PageResponse<CouponListForUserGetResponse> getCouponListForUser(Pageable pageable) {

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        // 1. 캐시에서 먼저 조회
        PageResponse<CouponListForUserGetResponse> cachedResponse = userCouponCacheService.getUserCouponListCache(pageNumber, pageSize);
        if (cachedResponse != null) {
            log.info("Cache HIT");
            return cachedResponse;
        }

        // 2. 캐시 없으면 DB 조회
        log.info("Cache MISS");
        Page<CouponListForUserGetResponse> couponPage = couponRepository
                .findAllAvailableCoupons(LocalDateTime.now(), pageable)
                .map(CouponListForUserGetResponse::from);

        PageResponse<CouponListForUserGetResponse> response = PageResponse.from(couponPage);

        // 3. 캐시에 저장
        userCouponCacheService.saveUserCouponListCache(pageNumber, pageSize, response);

        return response;
    }


    /**
     * 쿠폰 발급 로직
     */
    @Transactional
    public UserCouponIssueResponse issueCoupon(Long userId, Long couponId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponRepository.findByIdWithLock(couponId)
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
    @Transactional
    public PageResponse<UserCouponListGetResponse> getMyCouponList(Long userId, Pageable pageable, UserCouponStatus status) {

        Page<UserCoupon> userCouponPage = userCouponRepository.findAllActiveCouponsByUserId(userId, pageable, status);

        // AVAILABLE 조회 시에만 만료 체크 로직 실행
        if (status == UserCouponStatus.AVAILABLE) {
            LocalDateTime now = LocalDateTime.now();
            userCouponPage.getContent().forEach(uc -> {
                if (uc.getExpiredAt() != null && uc.getExpiredAt().isBefore(now)) {
                    uc.changeStatus(UserCouponStatus.EXPIRED);
                    userCouponRepository.save(uc);
                }
            });
        }

        return PageResponse.from(userCouponPage.map(UserCouponListGetResponse::from));
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
        if (userCoupon.getStatus() == UserCouponStatus.UNUSED_DELETED || userCoupon.getStatus() == UserCouponStatus.USED_DELETED) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_DELETED);
        }

        // if) 미사용 삭제 : 수량 복구 + DELETED 상태로 변경
        // else if)  사용후 삭제: 수량 복구 X + USED_DELETED 상태로 변경
        if (userCoupon.getStatus() == UserCouponStatus.AVAILABLE) {
            userCoupon.getCoupon().decreaseIssuedQuantity();
            userCoupon.changeStatus(UserCouponStatus.UNUSED_DELETED);
        } else if (userCoupon.getStatus() == UserCouponStatus.USED) {
            userCoupon.changeStatus(UserCouponStatus.USED_DELETED);
        }

        return UserCouponDeleteResponse.from(userCoupon);
    }
}