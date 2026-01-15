package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeDealRepository extends JpaRepository<TimeDeal, Long>, TimeDealCustomRepository {

    Page<TimeDeal> findAllByIsDeletedFalse(Pageable pageable);

}
