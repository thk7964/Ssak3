package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateStatusRequest;
import com.example.ssak3.domain.product.service.ProductAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/ssak3/admin/products")
@RestController
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductAdminService productAdminService;

    /**
     * 상품 생성 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createProductApi(@Valid @RequestBody ProductCreateRequest request) {

        ApiResponse response = ApiResponse.success("상품 생성에 성공했습니다.", productAdminService.createProduct(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 상품 상세조회(관리자) API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductAdminApi(@PathVariable Long productId) {

        ApiResponse response = ApiResponse.success("상품 상세 조회에 성공했습니다.", productAdminService.getProductAdmin(productId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 목록조회(관리자) API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getProductListAdminApi(@RequestParam(name = "categoryId", required = false) Long categoryId, @RequestParam(required = false) String status, @PageableDefault Pageable pageable) {

        ApiResponse response = ApiResponse.success("상품 목록 조회에 성공했습니다.", productAdminService.getProductListAdmin(categoryId, status, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품수정 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProductApi(@PathVariable Long productId, @Valid @RequestBody ProductUpdateRequest request) {

        ApiResponse response = ApiResponse.success("상품 수정에 성공했습니다.", productAdminService.updateProduct(productId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품삭제 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProductApi(@PathVariable Long productId) {

        ApiResponse response = ApiResponse.success("상품 삭제에 성공했습니다", productAdminService.deleteProduct(productId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 상태변경 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse> updateProductStatusApi(@RequestBody ProductUpdateStatusRequest request) {

        ApiResponse response = ApiResponse.success("상품의 상태가 변경되었습니다.", productAdminService.updateProductStatus(request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
