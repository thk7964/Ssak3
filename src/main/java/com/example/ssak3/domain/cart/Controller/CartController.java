package com.example.ssak3.domain.cart.Controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    /**
     * 내 장바구니 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getMyCartApi() {

        // 토큰이 없어서 임시 유저를 사용합니다. 추후 수정 예정
        ApiResponse response = ApiResponse.success("장바구니 조회에 성공했습니다.", cartService.getMyCart(1L));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
