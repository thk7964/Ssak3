package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
import com.example.ssak3.domain.product.model.request.ProductUpdateStatusRequest;
import com.example.ssak3.domain.product.model.response.*;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRankingService productRankingService;

    /**
     * 상품생성
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        // 카테고리 존재여부 확인 및 객체 가져오기
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
                .orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product(
                findCategory,
                request.getName(),
                request.getPrice(),
                ProductStatus.BEFORE_SALE,
                request.getInformation(),
                request.getQuantity()
        );
        Product createdProduct = productRepository.save(product);
        return ProductCreateResponse.from(createdProduct);
    }

    /**
     * 상품 상세조회(사용자)
     */
    @Transactional
    public ProductGetResponse getProduct(Long productId) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (foundProduct.getStatus().equals(ProductStatus.STOP_SALE)) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_VIEWABLE);
        }

        if (foundProduct.getStatus().equals(ProductStatus.BEFORE_SALE)) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_VIEWABLE);
        }

        try {
            productRankingService.increaseViewCount(productId);
        } catch (Exception e) {
            log.warn("Redis 조회수 업데이트 실패: productId = {}", foundProduct.getId());
        }

        return ProductGetResponse.from(foundProduct);
    }

    /**
     * 상품 상세조회(관리자)
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProductAdmin(Long productId) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductGetResponse.from(foundProduct);
    }

    /**
     * 상품 목록조회(사용자)
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductListGetResponse> getProductList(Long categoryId, Pageable pageable) {

        Page<ProductListGetResponse> productList = productRepository.findProductListByCategoryId(categoryId, pageable)
                .map(ProductListGetResponse::from);

       return PageResponse.from(productList);
    }

    /**
     * 상품 목록조회(관리자)
     */
    @Transactional(readOnly = true)
    public Object getProductListAdmin(Long categoryId, Pageable pageable) {
        Page<ProductListGetResponse> productList = productRepository.findProductListByCategoryIdForAdmin(categoryId, pageable)
                .map(ProductListGetResponse::from);
        return PageResponse.from(productList);
    }

    /**
     * 상품수정
     */
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));


        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        foundProduct.update(request, category);

        return ProductUpdateResponse.from(foundProduct);
    }

    /**
     * 상품삭제
     */
    @Transactional
    public ProductDeleteResponse deleteProduct(Long productId) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        foundProduct.softDelete();
        return ProductDeleteResponse.from(foundProduct);
    }

    /**
     * 상품 상태변경
     */
    @Transactional
    public ProductUpdateStatusResponse updateProductStatus(ProductUpdateStatusRequest request) {
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        foundProduct.updateStatus(request.getStatus());
        return ProductUpdateStatusResponse.from(foundProduct);
    }

}
