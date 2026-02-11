package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TimeDealRepository extends JpaRepository<TimeDeal, Long>, TimeDealCustomRepository {

    Optional<TimeDeal> findByIdAndIsDeletedFalse(Long timeDealId);

    List<TimeDeal> findAllByIdInAndIsDeletedFalse(List<Long> ids);

}
