package com.example.ssak3.domain.coupon.service;

import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.coupon.model.response.CouponListGetResponse;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponUserService {

    private final CouponRepository couponRepository;

    /**
     * 쿠폰 목록 조회 로직
     */
    @Transactional(readOnly = true)
    public PageResponse<CouponListGetResponse> getCouponList(Pageable pageable) {

        Page<CouponListGetResponse> couponListPage = couponRepository.findAllAvailableCoupons(LocalDateTime.now(), pageable)
                .map(CouponListGetResponse::from);

        return PageResponse.from(couponListPage);
    }

}
