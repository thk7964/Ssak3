package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeDealCustomRepository {

    Page<TimeDealListGetResponse> findTimeDeals (TimeDealStatus status, Pageable pageable);

    boolean existsActiveDealByProduct(Long productId);

    List<TimeDeal> findReadyToOpen(LocalDateTime now);

    List<TimeDeal> findOpenToClose(LocalDateTime now);
}
