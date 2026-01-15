package com.example.ssak3.domain.coupon.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.coupon.model.request.CouponCreateRequest;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import com.example.ssak3.domain.coupon.model.response.CouponCreateResponse;
import com.example.ssak3.domain.coupon.model.response.CouponDeleteResponse;
import com.example.ssak3.domain.coupon.model.response.CouponListGetResponse;
import com.example.ssak3.domain.coupon.model.response.CouponUpdateResponse;
import com.example.ssak3.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssac3/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createCouponApi(@RequestBody CouponCreateRequest request) {

        CouponCreateResponse response = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("쿠폰 생성 완료", response));
    }

    /**
     * 쿠폰 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getCouponListApi() {

        List<CouponListGetResponse> response = couponService.getCouponList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("쿠폰 목록 조회 완료", response));
    }

    /**
     * 쿠폰 수정
     */
    @PatchMapping("/{couponId}")
    public ResponseEntity<ApiResponse> updateCouponApi(@PathVariable Long couponId, @RequestBody CouponUpdateRequest request) {

        CouponUpdateResponse response = couponService.updateCoupon(couponId, request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("쿠폰 수정 완료", response));
    }

    /**
     * 쿠폰 삭제
     */
    @DeleteMapping("/{couponId}")
    public ResponseEntity<ApiResponse> deleteCouponApi(@PathVariable Long couponId) {

        CouponDeleteResponse response = couponService.deleteCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("쿠폰 삭제 완료", response));
    }
}
