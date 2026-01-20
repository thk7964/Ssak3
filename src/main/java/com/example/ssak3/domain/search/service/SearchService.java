package com.example.ssak3.domain.search.service;

import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.search.model.response.ProductSearchResponse;
import com.example.ssak3.domain.search.repository.SearchCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchCustomRepository searchCustomRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductSearchResponse> searchProduct(String keyword, Pageable pageable) {

        Page<ProductSearchResponse> response = searchCustomRepository.searchProduct(keyword, pageable);

        return PageResponse.from(response);
    }
}
