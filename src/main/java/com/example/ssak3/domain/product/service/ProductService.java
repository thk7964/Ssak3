package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
import com.example.ssak3.domain.product.model.response.*;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 상품생성
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, AuthUser user) {
        // 관리자의 존재여부 확인
        userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        // 카테고리 존재여부 확인 및 객체 가져오기
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
                .orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product(
                findCategory,
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
     * 상품 상세조회
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProduct(Long productId) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductGetResponse.form(foundProduct);
    }

    /**
     * 상품 목록조회
     */
    @Transactional(readOnly = true)
    public ProductListGetResponse getProductList(Long categoryId, Pageable pageable) {
        Page<Product> productList = productRepository.findProductListPage(categoryId, pageable);
       List<ProductListGetResponse.ProductDto> productDtoList = productList.getContent().stream()
                .map(product -> new ProductListGetResponse.ProductDto(
                        product.getId(),
                        product.getCategory().getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .toList();
       return new ProductListGetResponse(productList.getNumberOfElements(), productDtoList);


    }

    /**
     * 상품수정 비즈니스 로직
     */
    @Transactional
    public ProductUpdateResponse updateProduct(AuthUser user, Long productId, ProductUpdateRequest request) {
        // 관리자의 존재여부 확인
        userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.update(request);

        return ProductUpdateResponse.form(foundProduct);
    }

    /**
     * 상품삭제 비즈니스 로직
     */
    @Transactional
    public ProductDeleteResponse deleteProduct(AuthUser user, Long productId) {
        // 관리자의 존재여부 확인
        userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        // 상품 존재여부 확인
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 상품삭제(SoftDelete)
        foundProduct.softDelete();
       return ProductDeleteResponse.form(foundProduct);
    }
}
