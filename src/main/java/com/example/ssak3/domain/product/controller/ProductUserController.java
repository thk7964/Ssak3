package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.service.ProductUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/ssak3")
@RestController
@RequiredArgsConstructor
public class ProductUserController {

    private final ProductUserService productUserService;

    /**
     * 상품 상세조회(사용자) API
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> getProductApi(@PathVariable Long productId, HttpServletRequest request) {
        log.info("controller 상품상세조회 id: {}", productId);

        String ip = request.getRemoteAddr();

        ApiResponse response = ApiResponse.success("상품을 조회했습니다.", productUserService.getProduct(productId, ip));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 목록조회(사용자) API
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getProductListApi(
            // 카테고리 아이디값을 활용해서 해당 아이디 보유한 상품만 조회하는 기능
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("controller 상품목록조회 검색: {}", categoryId);
        ApiResponse response = ApiResponse.success("상품목록을 조회했습니다.", productUserService.getProductList(categoryId, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
