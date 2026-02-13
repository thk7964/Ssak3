package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;
import com.example.ssak3.domain.product.model.response.ProductGetSearchResponse;
import com.example.ssak3.domain.product.repository.ProductSearchCustomRepository;
import com.example.ssak3.domain.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchCustomRepository productSearchCustomRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public PageResponse<ProductGetSearchResponse> searchProduct(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable) {

        Page<ProductGetSearchResponse> response = productSearchCustomRepository.searchProduct(keyword, minPrice, maxPrice, pageable);

        Page<ProductGetSearchResponse> mapped = response.map(product ->
                new ProductGetSearchResponse(
                        product.getProductId(),
                        product.getTimeDealId(),
                        product.getCategoryId(),
                        product.getName(),
                        product.getInformation(),
                        product.getPrice(),
                        product.getDealPrice(),
                        s3Uploader.createPresignedGetUrl(product.getImageUrl(), 5),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                )
        );

        return PageResponse.from(mapped);

    }
}
