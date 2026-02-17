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
import com.example.ssak3.domain.usercoupon.service.UserCouponCacheService;
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
    private final UserCouponCacheService userCouponCacheService;

    /**
     * 쿠폰 생성
     */
    @Transactional
    public CouponCreateResponse createCoupon(CouponCreateRequest request) {

        if (couponRepository.existsByNameAndIsDeletedFalse(request.getName().trim())) {
            throw new CustomException(ErrorCode.COUPON_NAME_ALREADY_EXISTS);
        }

        if (request.getDiscountValue() > request.getMinOrderPrice()) {
            throw new CustomException(ErrorCode.DISCOUNT_EXCEEDS_MIN_ORDER_PRICE);
        }

        if (request.getIssueStartDate().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new CustomException(ErrorCode.COUPON_INVALID_START_TIME);
        }

        if (request.getIssueEndDate().isBefore(request.getIssueStartDate())) {
            throw new CustomException(ErrorCode.COUPON_INVALID_TIME_RANGE);
        }

        Coupon coupon = new Coupon(
                request.getName().trim(),
                request.getDiscountValue(),
                request.getTotalQuantity(),
                request.getIssueStartDate(),
                request.getIssueEndDate(),
                request.getMinOrderPrice(),
                request.getValidDays()
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        userCouponCacheService.clearUserCouponListCache();

        return CouponCreateResponse.from(savedCoupon);
    }

    /**
     * 쿠폰 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<CouponListGetResponse> getCouponList(Pageable pageable) {

        Page<CouponListGetResponse> couponListPage = couponRepository.findAllByIsDeletedFalse(pageable)
                .map(CouponListGetResponse::from);

        return PageResponse.from(couponListPage);
    }

    /**
     * 쿠폰 수정
     */
    @Transactional
    public CouponUpdateResponse updateCoupon(Long couponId, CouponUpdateRequest request) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if (request.getIssueEndDate().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new CustomException(ErrorCode.COUPON_INVALID_END_TIME);
        }

        if (request.getIssueEndDate().isBefore(coupon.getIssueStartDate())) {
            throw new CustomException(ErrorCode.COUPON_INVALID_TIME_RANGE);
        }

        coupon.update(request);

        userCouponCacheService.clearUserCouponListCache();

        return CouponUpdateResponse.from(coupon);
    }

    /**
     * 쿠폰 삭제
     */
    @Transactional
    public CouponDeleteResponse deleteCoupon(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        if (coupon.isDeleted()) {
            throw new CustomException(ErrorCode.COUPON_ALREADY_DELETED);
        }

        coupon.softDelete();

        userCouponCacheService.clearUserCouponListCache();

        return CouponDeleteResponse.from(coupon);
    }

}
