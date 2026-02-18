package com.example.ssak3.domain.productviewhistory.repository;

import com.example.ssak3.domain.productviewhistory.entity.ProductViewHistory;

import java.util.List;

public interface ProductViewHistoryCustomRepository {

    void saveAll(List<ProductViewHistory> histories);
}
