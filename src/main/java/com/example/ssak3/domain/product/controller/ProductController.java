package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
import com.example.ssak3.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@Slf4j
@RequestMapping("/ssak3")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품생성 API
     */
    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse> createProductApi(
            @AuthenticationPrincipal AuthUser user,
            @RequestBody ProductCreateRequest request
    ) {
        log.info("controller 상품생성 상품명: {}", request.getName());
        ApiResponse response = ApiResponse.success("상품을 생성하셨습니다.", productService.createProduct(request, user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /**
     * 상품 상세조회 API
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> getProductApi(
            @PathVariable Long productId
            ) {
        log.info("controller 상품상세조회 id: {}", productId);
       ApiResponse response = ApiResponse.success("상품을 조회했습니다.", productService.getProduct(productId));
       return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    /**
     * 상품 목록조회 API
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getProductListApi(
            @RequestParam(name = "name")String name,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        log.info("controller 상품목록조회 검색: {}", name);
        ApiResponse response = ApiResponse.success("상품목록을 조회했습니다.",  productService.getProductList(name, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    /**
     * 상품수정 API
     */
    @PatchMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> updateProductApi(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request) {
        log.info("controller 상품수정 id: {}", productId);
        ApiResponse response = ApiResponse.success("상품을 수정하였습니다.", productService.updateProduct(user, productId, request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품삭제 API
     */
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProductApi(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long productId) {
        log.info("controller 상품삭제 id: {}", productId);
        ApiResponse response = ApiResponse.success("상품을 삭제하였습니다", productService.deleteProduct(user, productId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




}
