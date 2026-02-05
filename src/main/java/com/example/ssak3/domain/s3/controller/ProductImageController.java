package com.example.ssak3.domain.s3.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.s3.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/products/{productId}/images")
public class ProductImageController {

    private final ProductImageService productImageService;

    /**
     * 상품 이미지 업로드
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createProductImageApi(@PathVariable Long productId, @RequestPart(value = "image") MultipartFile file) throws IOException {

        ApiResponse response = ApiResponse.success("상품 이미지 저장에 성공했습니다.", productImageService.uploadProductImage(productId, file));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 상품 이미지 가져오기
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getProductImageApi(@PathVariable Long productId){
        ApiResponse response = ApiResponse.success("상품 이미지 조회에 성공했습니다.", productImageService.getProductImage(productId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 이미지 수정
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ApiResponse> UpdateProductImageApi(@PathVariable Long productId, @RequestPart(value = "image")  MultipartFile file) throws IOException {

        ApiResponse response = ApiResponse.success("상품 이미지 변경에 성공했습니다.", productImageService.updateProductImage(productId, file));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 상품 이미지 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteProductImageApi(@PathVariable Long productId){

        ApiResponse response = ApiResponse.success("상품 이미지 삭제에 성공했습니다.", productImageService.deleteProductImage(productId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
