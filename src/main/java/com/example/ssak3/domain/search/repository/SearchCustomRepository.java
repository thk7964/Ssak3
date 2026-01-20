package com.example.ssak3.domain.search.repository;

import com.example.ssak3.domain.search.model.response.ProductSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchCustomRepository {

    Page<ProductSearchResponse> searchProduct(String keyword, Pageable pageable);
}
