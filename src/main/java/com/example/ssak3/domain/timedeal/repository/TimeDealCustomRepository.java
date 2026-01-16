package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TimeDealCustomRepository {

    Page<TimeDealListGetResponse> findOpenTimeDeals (Pageable pageable);

    boolean existsActiveDealByProduct(Long productId, LocalDateTime now);
}
