package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.service.ProductUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/products")
public class ProductUserController {

    private final ProductUserService productUserService;

    /**
     * 상품 상세 조회(사용자) API
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> getProductApi(@PathVariable Long productId, HttpServletRequest request) {

        ApiResponse response = ApiResponse.success("상품 조회에 성공했습니다.", productUserService.getProduct(productId, request.getRemoteAddr()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 목록조회(사용자) API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getProductListApi(@RequestParam(name = "categoryId", required = false) Long categoryId, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("상품 목록 조회에 성공했습니다.", productUserService.getProductList(categoryId, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
