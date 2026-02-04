package com.example.ssak3.domain.productviewhistory.repository;

import com.example.ssak3.domain.productviewhistory.entity.ProductViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductViewHistoryRepository extends JpaRepository<ProductViewHistory, Long>, ProductViewHistoryCustomRepository {

}