package com.example.ssak3.domain.category.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.category.service.CategoryUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssak3/categories")
@RequiredArgsConstructor
public class CategoryUserController {

    private final CategoryUserService categoryUserService;

    /**
     * 카테고리 목록조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getCategoryListApi() {
        ApiResponse response = ApiResponse.success("카테고리목록을 조회했습니다.", categoryUserService.getCategoryList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
