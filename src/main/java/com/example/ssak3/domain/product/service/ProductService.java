package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
import com.example.ssak3.domain.product.model.response.*;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품생성 비즈니스 로직
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {

        Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getStatus(),
                request.getInformation(),
                request.getQuantity()
        );
        Product createdProduct = productRepository.save(product);
        return ProductCreateResponse.from(createdProduct);
    }

    /**
     * 상품 상세조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProduct(Long productId) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductGetResponse.form(foundProduct);
    }

    /**
     * 상품 목록조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public ProductGetListResponse getProductList(String name, Pageable pageable) {
        Page<Product> productList = productRepository.findProductListPage(name, pageable);
       List<ProductGetListResponse.ProductDto> productDtoList = productList.getContent().stream()
                .map(product -> new ProductGetListResponse.ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getInformation(),
                        product.getQuantity(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();
       return new ProductGetListResponse(productList.getNumberOfElements(), productDtoList);


    }

    /**
     * 상품수정 비즈니스 로직
     */
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.update(request);

        return ProductUpdateResponse.form(foundProduct);
    }

    /**
     * 상품삭제 비즈니스 로직
     */
    @Transactional
    public ProductDeleteResponse deleteProduct(Long productId) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

       if(!productRepository.existsByIdAndIsDeletedFalse(foundProduct.getId())) {
           throw new RuntimeException("이미 삭제된 상품입니다");
       }
        foundProduct.isDeleted();
       return ProductDeleteResponse.form(foundProduct);
    }
}
