package com.example.ssak3.domain.product.repository;

import com.example.ssak3.domain.product.model.response.ProductGetSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearchCustomRepository {

    Page<ProductGetSearchResponse> searchProduct(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable);
}
