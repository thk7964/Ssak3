package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateStatusRequest;
import com.example.ssak3.domain.product.service.ProductRankingService;
import com.example.ssak3.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/ssak3")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRankingService productRankingService;

    /**
     * 상품생성 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse> createProductApi(
            @RequestBody ProductCreateRequest request
    ) {
        log.info("controller 상품생성 상품명: {}", request.getName());
        ApiResponse response = ApiResponse.success("상품을 생성하셨습니다.", productService.createProduct(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 상품 상세조회(사용자) API
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> getProductApi(@PathVariable Long productId) {
        log.info("controller 상품상세조회 id: {}", productId);
       ApiResponse response = ApiResponse.success("상품을 조회했습니다.", productService.getProduct(productId));
       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 상세조회(관리자) API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> getProductAdminApi(@PathVariable Long productId) {
        log.info("controller 상품상세조회 - admin id: {}", productId);
        ApiResponse response = ApiResponse.success("상품을 조회했습니다.", productService.getProductAdmin(productId));
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
        ApiResponse response = ApiResponse.success("상품목록을 조회했습니다.",  productService.getProductList(categoryId, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 목록조회(관리자) API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products")
    public ResponseEntity<ApiResponse> getProductListAdminApi(
            // 카테고리 아이디값을 활용해서 해당 아이디 보유한 상품만 조회하는 기능
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("controller 상품목록조회 검색 - admin 카테고리Id: {}", categoryId);
        ApiResponse response = ApiResponse.success("상품목록을 조회했습니다.",  productService.getProductListAdmin(categoryId, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품수정 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> updateProductApi(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request) {
        log.info("controller 상품수정 id: {}", productId);
        ApiResponse response = ApiResponse.success("상품을 수정하였습니다.", productService.updateProduct(productId, request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품삭제 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProductApi(
            @PathVariable Long productId) {
        log.info("controller 상품삭제 id: {}", productId);
        ApiResponse response = ApiResponse.success("상품을 삭제하였습니다", productService.deleteProduct(productId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 조회 수 TOP 10
     */
    @GetMapping("/products/popular")
    public ResponseEntity<ApiResponse> getPopularProductApi() {

        ApiResponse response = ApiResponse.success("조회 수 인기 TOP 10 상품 검색에 성공했습니다.", productRankingService.getPopularProductTop10());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 상태변경 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/products/status")
    public ResponseEntity<ApiResponse> updateProductStatusApi(@RequestBody ProductUpdateStatusRequest request) {
        ApiResponse response = ApiResponse.success("상품의 상태가 변경되었습니다.", productService.updateProductStatus(request));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
