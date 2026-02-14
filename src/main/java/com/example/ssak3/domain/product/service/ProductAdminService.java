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
public class ProductAdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRankingService productRankingService;
    private final S3Uploader s3Uploader;

    /**
     * 상품생성
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        // 카테고리 존재여부 확인 및 객체 가져오기
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product(
                findCategory,
                request.getName(),
                request.getPrice(),
                ProductStatus.BEFORE_SALE,
                request.getInformation(),
                request.getQuantity(),
                request.getImage(),
                request.getDetailImage()
        );

        Product createdProduct = productRepository.save(product);
        return ProductCreateResponse.from(createdProduct);
    }

    /**
     * 상품 상세조회(관리자)
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProductAdmin(Long productId) {
        log.info("product service 조회 전: {}", productId);
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        String viewImageUrl = s3Uploader.createPresignedGetUrl(foundProduct.getImage(), 5);
        String viewDetailImageUrl = s3Uploader.createPresignedGetUrl(foundProduct.getDetailImage(), 5);

        return ProductGetResponse.from(foundProduct, viewImageUrl, viewDetailImageUrl);
    }

    /**
     * 상품 목록조회(관리자)
     */
    @Transactional(readOnly = true)
    public PageResponse<ProductListGetResponse> getProductListAdmin(Long categoryId, Pageable pageable) {
        Page<Product> productList = productRepository.findProductListByCategoryIdForAdmin(categoryId, pageable);

        Page<ProductListGetResponse> mapped = productList.map(p -> {
            String viewImageUrl = s3Uploader.createPresignedGetUrl(p.getImage(), 5);
            return ProductListGetResponse.from(p, viewImageUrl);
        });

        return PageResponse.from(mapped);
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

        // 저장되어 있는 이미지 파일이 있는지 확인
        if (request.getImage()!=null && foundProduct.getImage() != null) {
            // 기존 파일 먼저 삭제
            s3Uploader.deleteImage(foundProduct.getImage());
        }

        // 저장되어 있는 이미지 파일이 있는지 확인
        if (request.getDetailImage()!=null && foundProduct.getDetailImage() != null) {
            // 기존 파일 먼저 삭제
            s3Uploader.deleteImage(foundProduct.getDetailImage());
        }

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
     * 상품 상태변경
     */
    @Transactional
    public ProductUpdateStatusResponse updateProductStatus(ProductUpdateStatusRequest request) {
        log.info("productService 상태변경 전: {}", request.getStatus());
        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.updateStatus(request.getStatus());
        log.info("productService 상태변경 후: {}", foundProduct.getStatus());
        return ProductUpdateStatusResponse.from(foundProduct);
    }

}
