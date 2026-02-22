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
@RequestMapping("/ssak3/admin/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 목록 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getCouponListApi(@PageableDefault(sort = "issueEndDate", direction = Sort.Direction.ASC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("쿠폰 목록 조회를 성공했습니다.", couponService.getCouponList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 쿠폰 생성 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createCouponApi(@Valid @RequestBody CouponCreateRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰을 생성에 성공했습니다.", couponService.createCoupon(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 쿠폰 수정 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{couponId}")
    public ResponseEntity<ApiResponse> updateCouponApi(@PathVariable Long couponId, @RequestBody CouponUpdateRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰을 수정에 성공했습니다.", couponService.updateCoupon(couponId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 쿠폰 삭제 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{couponId}")
    public ResponseEntity<ApiResponse> deleteCouponApi(@PathVariable Long couponId) {

        ApiResponse response = ApiResponse.success("쿠폰을 삭제에 성공했습니다.", couponService.deleteCoupon(couponId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
