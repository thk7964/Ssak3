package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/products/search")
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    /**
     * 상품 통합 검색 (상품명 키워드, 가격 범위)
     */
    @GetMapping
    public ResponseEntity<ApiResponse> searchProductApi(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @PageableDefault Pageable pageable) {

        ApiResponse response = ApiResponse.success("상품 통합 검색에 성공했습니다.", productSearchService.searchProduct(keyword, minPrice, maxPrice, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
