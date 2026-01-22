package com.example.ssak3.domain.coupon.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.coupon.model.request.CouponCreateRequest;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import com.example.ssak3.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ssak3")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 생성
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/coupons")
    public ResponseEntity<ApiResponse> createCouponApi(@Valid @RequestBody CouponCreateRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰 생성 완료", couponService.createCoupon(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 쿠폰 목록 조회
     */
    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse> getCouponListApi(@PageableDefault(size = 10, sort = "issueEndDate", direction = Sort.Direction.ASC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("쿠폰 목록 조회 완료", couponService.getCouponList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 쿠폰 수정
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse> updateCouponApi(@PathVariable Long couponId, @RequestBody CouponUpdateRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰 수정 완료", couponService.updateCoupon(couponId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 쿠폰 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse> deleteCouponApi(@PathVariable Long couponId) {

        ApiResponse response = ApiResponse.success("쿠폰 삭제 완료", couponService.deleteCoupon(couponId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
