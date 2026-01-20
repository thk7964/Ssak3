package com.example.ssak3.domain.search.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.search.service.SearchService;
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
@RequestMapping("/ssak3/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ApiResponse> searchProductApi(
            @RequestParam(required = false) String keyword,
            @PageableDefault Pageable pageable) {

        ApiResponse response = ApiResponse.success("상품 통합 검색에 성공했습니다.", searchService.searchProduct(keyword, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
