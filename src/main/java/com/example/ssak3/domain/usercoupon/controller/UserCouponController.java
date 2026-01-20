package com.example.ssak3.domain.usercoupon.controller;

import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.usercoupon.model.request.UserCouponIssueRequest;
import com.example.ssak3.domain.usercoupon.service.UserCouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/ssak3/user-coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    /**
     * 쿠폰 추가
     */
    @PostMapping
    public ResponseEntity<ApiResponse> issueCouponApi(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UserCouponIssueRequest request) {

        ApiResponse response = ApiResponse.success("쿠폰 발급 완료", userCouponService.issueCoupon(authUser.getId(), request.getCouponId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내 쿠폰 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getMyCouponListApi(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(size = 10, sort = "coupon.discountValue", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "AVAILABLE") UserCouponStatus status) {

        ApiResponse response = ApiResponse.success("내 쿠폰 조회 완료", userCouponService.getMyCouponList(authUser.getId(), pageable, status));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 쿠폰 사용 처리
     */
    @PatchMapping("/{userCouponId}")
    public ResponseEntity<ApiResponse> useCouponApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long userCouponId) {

        ApiResponse response = ApiResponse.success("내 쿠폰 사용 완료", userCouponService.useCoupon(authUser.getId(), userCouponId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
