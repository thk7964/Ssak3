package com.example.ssak3.domain.coupon.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.model.request.CouponCreateRequest;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import com.example.ssak3.domain.coupon.model.response.CouponCreateResponse;
import com.example.ssak3.domain.coupon.model.response.CouponDeleteResponse;
import com.example.ssak3.domain.coupon.model.response.CouponListGetResponse;
import com.example.ssak3.domain.coupon.model.response.CouponUpdateResponse;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성 로직
     */
    @Transactional
    public CouponCreateResponse createCoupon(CouponCreateRequest request) {

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
     * 쿠폰 목록 조회 로직
     */
    @Transactional(readOnly = true)
    public PageResponse<CouponListGetResponse> getCouponList(Pageable pageable) {

        Page<CouponListGetResponse> couponListPage = couponRepository.findAllAvailableCoupons(LocalDateTime.now(), pageable)
                .map(CouponListGetResponse::from);

        return PageResponse.from(couponListPage);
    }

    /**
     * 쿠폰 수정 로직
     */
    @Transactional
    public CouponUpdateResponse updateCoupon(Long couponId, CouponUpdateRequest request) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

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
