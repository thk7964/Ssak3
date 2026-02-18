package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/products/popular")
public class ProductRankingController {

    private final ProductRankingService productRankingService;

    /**
     * 조회수 TOP 10 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getPopularProductApi() {

        ApiResponse response = ApiResponse.success("조회 수 인기 TOP 10 상품 검색에 성공했습니다.", productRankingService.getPopularProductTop10());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
