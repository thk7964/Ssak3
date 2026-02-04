package com.example.ssak3.domain.coupon.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.coupon.service.CouponUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssak3/coupons")
@RequiredArgsConstructor
public class CouponUserController {

    private final CouponUserService couponUserService;

    /**
     * 쿠폰 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getCouponListApi(@PageableDefault(size = 10, sort = "issueEndDate", direction = Sort.Direction.ASC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("쿠폰 목록 조회 완료", couponUserService.getCouponList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
