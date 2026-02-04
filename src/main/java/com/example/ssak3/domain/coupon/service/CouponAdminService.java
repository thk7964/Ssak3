package com.example.ssak3.domain.coupon.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.model.request.CouponCreateRequest;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import com.example.ssak3.domain.coupon.model.response.CouponCreateResponse;
import com.example.ssak3.domain.coupon.model.response.CouponDeleteResponse;
import com.example.ssak3.domain.coupon.model.response.CouponUpdateResponse;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponAdminService {

    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성 로직
     */
    @Transactional
    public CouponCreateResponse createCoupon(CouponCreateRequest request) {

        // 시작일이 현재 시간보다 이전인 경우 예외
        if (request.getIssueStartDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.COUPON_INVALID_START_TIME);
        }

        // 종료일이 시작일보다 빠른 경우 예외
        if (request.getIssueEndDate().isBefore(request.getIssueStartDate())) {
            throw new CustomException(ErrorCode.COUPON_INVALID_TIME_RANGE);
        }

        Coupon coupon = new Coupon(
                request.getName(),
                request.getDiscountValue(),
                request.getTotalQuantity(),
                request.getIssueStartDate(),
                request.getIssueEndDate(),
                request.getMinOrderPrice(),
                request.getValidDays()
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        return CouponCreateResponse.from(savedCoupon);
    }

    /**
     * 쿠폰 수정 로직
     */
    @Transactional
    public CouponUpdateResponse updateCoupon(Long couponId, CouponUpdateRequest request) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 종료일이 현재 시간보다 이전으로 수정되는 것 방지
        if (request.getIssueEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.COUPON_INVALID_END_TIME);
        }

        // 종료일이 시작일보다 빠른 경우 예외
        if (request.getIssueEndDate().isBefore(coupon.getIssueStartDate())) {
            throw new CustomException(ErrorCode.COUPON_INVALID_TIME_RANGE);
        }

        coupon.update(request);

        return CouponUpdateResponse.from(coupon);
    }

    /**
     * 쿠폰 삭제 로직
     */
    @Transactional
    public CouponDeleteResponse deleteCoupon(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if (coupon.isDeleted()) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_DELETED);
        }
        coupon.delete();

        return CouponDeleteResponse.from(coupon);
    }

}
