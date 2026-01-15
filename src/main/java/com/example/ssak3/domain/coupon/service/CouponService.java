package com.example.ssak3.domain.coupon.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.model.request.CouponCreateRequest;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import com.example.ssak3.domain.coupon.model.response.CouponCreateResponse;
import com.example.ssak3.domain.coupon.model.response.CouponDeleteResponse;
import com.example.ssak3.domain.coupon.model.response.CouponListGetResponse;
import com.example.ssak3.domain.coupon.model.response.CouponUpdateResponse;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성 로직
     */
    @Transactional
    public CouponCreateResponse createCoupon(CouponCreateRequest request) {

        Coupon coupon = new Coupon(
                null,
                request.getName(),
                request.getDiscountValue(),
                request.getTotalQuantity(),
                0,
                request.getIssueStartDate(),
                request.getIssueEndDate(),
                request.getMinOrderPrice(),
                request.getValidDays(),
                false
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        return CouponCreateResponse.from(savedCoupon);
    }

    /**
     * 쿠폰 목록 조회 로직
     */
    @Transactional(readOnly = true)
    public List<CouponListGetResponse> getCouponList() {

        List<CouponListGetResponse> couponList = couponRepository.findAllByIsDeletedFalse().stream()
                .map(CouponListGetResponse::from)
                .toList();

        return couponList;
    }

    /**
     * 쿠폰 수정 로직
     */
    @Transactional
    public CouponUpdateResponse updateCoupon(Long couponId, CouponUpdateRequest request) {

        Coupon coupon = couponRepository.findByIdAndIsDeletedFalse(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        coupon.update(request);

        return CouponUpdateResponse.from(coupon);
    }

    /**
     * 쿠폰 삭제 로직
     */
    @Transactional
    public CouponDeleteResponse deleteCoupon(Long couponId) {

        Coupon coupon = couponRepository.findByIdAndIsDeletedFalse(couponId)
                .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        coupon.delete();

        return CouponDeleteResponse.from(coupon);
    }

}
