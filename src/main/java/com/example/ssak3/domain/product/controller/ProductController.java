package com.example.ssak3.domain.product.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품생성  API
     */
    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse> createProductApi(@RequestBody ProductCreateRequest request) {

        ApiResponse response = ApiResponse.success("상품을 생성하셨습니다.", productService.createProduct(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
