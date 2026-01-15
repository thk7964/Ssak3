package com.example.ssak3.domain.cartproduct.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.cartproduct.model.request.CartProductAddRequest;
import com.example.ssak3.domain.cartproduct.model.request.CartProductDeleteRequest;
import com.example.ssak3.domain.cartproduct.model.request.CartProductQuantityUpdateRequest;
import com.example.ssak3.domain.cartproduct.service.CartProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/carts")
public class CartProductController {

    private final CartProductService cartProductService;

    @PostMapping
    public ResponseEntity<ApiResponse> addCartProductAPI(@RequestBody CartProductAddRequest request) {

        // 토큰이 없어서 userId값을 1로 고정. 토큰 추가 후 수정 예정
        ApiResponse response = ApiResponse.success("장바구니 상품 담기에 성공했습니다.", cartProductService.addCartProduct(1L, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> updateCartProductQuantityAPI(@RequestBody CartProductQuantityUpdateRequest request) {

        ApiResponse response = ApiResponse.success("장바구니 상품 수량 변경에 성공했습니다.", cartProductService.updateCartProductQuantity(1L, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCartProductAPI(@RequestBody CartProductDeleteRequest request) {

        cartProductService.deleteCartProduct(1L, request);

        ApiResponse response = ApiResponse.success("장바구니 상품 삭제 성공했습니다.", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
