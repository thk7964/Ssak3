package com.example.ssak3.domain.cart.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/carts")
public class CartController {

    private final CartService cartService;

    /**
     * 내 장바구니 조회 API
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyCartApi(
            @AuthenticationPrincipal AuthUser user) {

        ApiResponse response = ApiResponse.success("장바구니 조회에 성공했습니다.", cartService.getMyCart(user.getId()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
