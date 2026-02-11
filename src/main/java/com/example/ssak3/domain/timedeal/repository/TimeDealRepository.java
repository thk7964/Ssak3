package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TimeDealRepository extends JpaRepository<TimeDeal, Long>, TimeDealCustomRepository {

    Optional<TimeDeal> findByIdAndIsDeletedFalse(Long timeDealId);

    Optional<TimeDeal> findByProductIdAndStatusAndIsDeletedFalse(Long productId, TimeDealStatus status);


}
