package com.example.ssak3.domain.s3.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.s3.model.response.ProductImageGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final S3Uploader s3Uploader;
    private final ProductRepository productRepository;

    /**
     * 상품 이미지 업로드
     */
    @Transactional
    public ProductImageGetResponse createProductImage(Long productId, MultipartFile multipartFile) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getImage() != null) {
            throw new CustomException(ErrorCode.PRODUCT_IMAGE_ALREADY_EXIST);
        }

        s3Uploader.uploadProductImage(product, multipartFile, "products");

        return ProductImageGetResponse.from(product);
    }

    /**
     * 상품 이미지 가져오기
     */
    @Transactional(readOnly = true)
    public ProductImageGetResponse getProductImage(Long productId) {

        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductImageGetResponse.from(product);
    }

    /**
     * 상품 이미지 수정
     */
    @Transactional
    public ProductImageGetResponse updateProductImage(Long productId, MultipartFile multipartFile) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        String oldUrl = product.getImage();

        s3Uploader.uploadProductImage(product, multipartFile, "products");
        s3Uploader.deleteImage(oldUrl);

        return ProductImageGetResponse.from(product);
    }


    /**
     * 상품 이미지 삭제
     */
    @Transactional
    public ProductImageGetResponse deleteProductImage(Long productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        s3Uploader.deleteImage(product.getImage());
        product.setImage(null);

        return ProductImageGetResponse.from(product);
    }

}
