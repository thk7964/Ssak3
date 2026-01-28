package com.example.ssak3.domain.product.repository;

import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;

import java.util.List;

public interface ProductCustomRepository {

    List<ProductGetPopularResponse> getProductViewCountTop10();
}
