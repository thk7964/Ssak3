package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.product.model.response.ProductGetSearchResponse;
import com.example.ssak3.domain.product.repository.ProductSearchCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchCustomRepository productSearchCustomRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductGetSearchResponse> searchProduct(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable) {

        Page<ProductGetSearchResponse> response = productSearchCustomRepository.searchProduct(keyword, minPrice, maxPrice, pageable);

        return PageResponse.from(response);
    }
}
