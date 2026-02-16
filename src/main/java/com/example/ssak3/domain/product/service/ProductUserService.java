package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.response.ProductGetResponse;
import com.example.ssak3.domain.product.model.response.ProductListGetResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUserService {

    private final ProductRepository productRepository;
    private final ProductRankingService productRankingService;
    private final S3Uploader s3Uploader;

    /**
     * 상품 상세 조회 (사용자)
     */
    @Transactional
    public ProductGetResponse getProduct(Long productId, String ip) {

        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (foundProduct.getStatus().equals(ProductStatus.STOP_SALE) || foundProduct.getStatus().equals(ProductStatus.BEFORE_SALE)) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_VIEWABLE);
        }

        try {
            productRankingService.increaseViewCount(productId, ip);
        } catch (Exception e) {
            log.warn("Redis 조회수 업데이트 실패: productId = {}", foundProduct.getId());
        }

        String viewImageUrl = s3Uploader.createPresignedGetUrl(foundProduct.getImage(), 5);
        String viewDetailImageUrl = s3Uploader.createPresignedGetUrl(foundProduct.getDetailImage(), 5);

        return ProductGetResponse.from(foundProduct, viewImageUrl, viewDetailImageUrl);
    }

    /**
     * 상품 목록 조회 (사용자)
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductListGetResponse> getProductList(Long categoryId, Pageable pageable) {

        Page<Product> productList = productRepository.findProductListByCategoryId(categoryId, pageable);

        Page<ProductListGetResponse> mapped = productList.map(p -> {
            String viewImageUrl = s3Uploader.createPresignedGetUrl(p.getImage(), 5);
            return ProductListGetResponse.from(p, viewImageUrl);
        });

        return PageResponse.from(mapped);
    }
}
