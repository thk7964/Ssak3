package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeDealRepository extends JpaRepository<TimeDeal, Long>, TimeDealCustomRepository {

    @Query("""
                    select td
                    from TimeDeal td
                    where td.product.id = :productId
                    and td.isDeleted = false
                    and td.startAt <= :now
                    and td.endAt >= :now
                    order by td.startAt desc
            """)
    Optional<TimeDeal> findOpenTimeDeal(@Param("productId") Long productId, @Param("now") LocalDateTime now);

    Optional<TimeDeal> findByIdAndIsDeletedFalse(Long timeDealId);

    List<TimeDeal> findAllByProductIdInAndStatusAndIsDeletedFalse(List<Long> productIds, TimeDealStatus timeDealStatus);
}
