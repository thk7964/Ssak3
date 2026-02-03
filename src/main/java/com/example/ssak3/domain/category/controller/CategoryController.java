package com.example.ssak3.domain.category.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.category.model.request.CategoryCreateRequest;
import com.example.ssak3.domain.category.model.request.CategoryUpdateRequest;
import com.example.ssak3.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ssak3")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 생성 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse> createCategoryApi(@RequestBody CategoryCreateRequest request) {
        ApiResponse response = ApiResponse.success("카테고리를 생성하였습니다.",  categoryService.createCategory(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 카테고리 목록조회 API
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getCategoryListApi() {
        ApiResponse response = ApiResponse.success("카테고리목록을 조회했습니다.", categoryService.getCategoryList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 카테고리 수정 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategoryApi(
            @PathVariable Long categoryId,
            @RequestBody CategoryUpdateRequest request) {
        ApiResponse response = ApiResponse.success("카테고리를 수정하였습니다.", categoryService.updateCategory(categoryId, request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 카테고리 삭제 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryApi(
            @PathVariable Long categoryId) {
        ApiResponse response = ApiResponse.success("카테고리를 삭제하였습니다.", categoryService.deleteCategory(categoryId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
