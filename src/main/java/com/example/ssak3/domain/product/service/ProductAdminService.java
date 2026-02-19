package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
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
import com.example.ssak3.domain.s3.service.S3Uploader;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductAdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TimeDealRepository timeDealRepository;
    private final S3Uploader s3Uploader;

    /**
     * 상품 생성
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {

        Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product(
                category,
                request.getName(),
                request.getPrice(),
                ProductStatus.BEFORE_SALE,
                request.getInformation(),
                request.getQuantity(),
                request.getImage(),
                request.getDetailImage()
        );

        Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.from(savedProduct);
    }

    /**
     * 상품 상세 조회(관리자)
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProductAdmin(Long productId) {

        Product product = findProduct(productId);

        String viewImageUrl = s3Uploader.createPresignedGetUrl(product.getImage(), 5);
        String viewDetailImageUrl = s3Uploader.createPresignedGetUrl(product.getDetailImage(), 5);

        return ProductGetResponse.from(product, viewImageUrl, viewDetailImageUrl);
    }

    /**
     * 상품 목록 조회(관리자)
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductListGetResponse> getProductListAdmin(Long categoryId, String status, Pageable pageable) {

        ProductStatus productStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                productStatus = ProductStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorCode.INVALID_PRODUCT_STATUS);
            }
        }

        Page<Product> productList = productRepository.findProductListForAdmin(categoryId, productStatus, pageable);

        Page<ProductListGetResponse> mapped = productList.map(p -> {
            String viewImageUrl = s3Uploader.createPresignedGetUrl(p.getImage(), 5);
            return ProductListGetResponse.from(p, viewImageUrl);
        });

        return PageResponse.from(mapped);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest request) {

        Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product foundProduct = findProduct(productId);

        if (request.getImage() != null && foundProduct.getImage() != null) {
            s3Uploader.deleteImage(foundProduct.getImage());
        }

        if (request.getDetailImage() != null && foundProduct.getDetailImage() != null) {
            s3Uploader.deleteImage(foundProduct.getDetailImage());
        }

        foundProduct.update(request, category);

        return ProductUpdateResponse.from(foundProduct);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public ProductDeleteResponse deleteProduct(Long productId) {

        Product foundProduct = findProduct(productId);

        boolean hasBlockingTimeDeal = timeDealRepository.existsByProductAndStatusInAndIsDeletedFalse(foundProduct, List.of(TimeDealStatus.READY, TimeDealStatus.OPEN));

        if (hasBlockingTimeDeal) {
            throw new CustomException(ErrorCode.PRODUCT_HAS_TIME_DEAL);
        }

        if (foundProduct.getImage() != null) {
            s3Uploader.deleteImage(foundProduct.getImage());
        }

        if (foundProduct.getDetailImage() != null) {
            s3Uploader.deleteImage(foundProduct.getDetailImage());
        }

        foundProduct.softDelete();

        return ProductDeleteResponse.from(foundProduct);
    }

    /**
     * 상품 상태 변경
     */
    @Transactional
    public ProductUpdateStatusResponse updateProductStatus(ProductUpdateStatusRequest request) {

        Product foundProduct = findProduct(request.getProductId());

        foundProduct.updateStatus(request.getStatus());

        return ProductUpdateStatusResponse.from(foundProduct);
    }

    private Product findProduct(Long productId) {
        return productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

}
