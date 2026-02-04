package com.example.ssak3.domain.coupon.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.coupon.model.request.CouponCreateRequest;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import com.example.ssak3.domain.coupon.service.CouponAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ssak3/admin/admin-coupons")
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    /**
     * 쿠폰 생성
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createCouponApi(@Valid @RequestBody CouponCreateRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰 생성 완료", couponAdminService.createCoupon(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 쿠폰 수정
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{couponId}")
    public ResponseEntity<ApiResponse> updateCouponApi(@PathVariable Long couponId, @RequestBody CouponUpdateRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰 수정 완료", couponAdminService.updateCoupon(couponId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 쿠폰 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{couponId}")
    public ResponseEntity<ApiResponse> deleteCouponApi(@PathVariable Long couponId) {

        ApiResponse response = ApiResponse.success("쿠폰 삭제 완료", couponAdminService.deleteCoupon(couponId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
