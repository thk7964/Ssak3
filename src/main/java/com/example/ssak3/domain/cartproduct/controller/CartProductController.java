package com.example.ssak3.domain.cartproduct.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.cartproduct.model.request.CartProductAddRequest;
import com.example.ssak3.domain.cartproduct.model.request.CartProductDeleteRequest;
import com.example.ssak3.domain.cartproduct.model.request.CartProductQuantityUpdateRequest;
import com.example.ssak3.domain.cartproduct.service.CartProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/carts")
public class CartProductController {

    private final CartProductService cartProductService;

    @PostMapping
    public ResponseEntity<ApiResponse> addCartProductAPI(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody CartProductAddRequest request) {

        ApiResponse response = ApiResponse.success("장바구니 상품 담기에 성공했습니다.", cartProductService.addCartProduct(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> updateCartProductQuantityAPI(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody CartProductQuantityUpdateRequest request) {

        ApiResponse response = ApiResponse.success("장바구니 상품 수량 변경에 성공했습니다.", cartProductService.updateCartProductQuantity(user.getId(), request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCartProductAPI(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody CartProductDeleteRequest request) {

        ApiResponse response = ApiResponse.success("장바구니 상품 삭제 성공했습니다.", cartProductService.deleteCartProduct(user.getId(), request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
