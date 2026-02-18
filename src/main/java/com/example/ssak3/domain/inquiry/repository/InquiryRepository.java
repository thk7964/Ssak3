package com.example.ssak3.domain.inquiry.repository;

import com.example.ssak3.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i " +
            "JOIN FETCH i.user " +
            "WHERE i.isDeleted = false " +
            "AND i.user.id = :userId")
    Page<Inquiry> findAllByIsDeletedFalseAndUserId(Long userId, Pageable pageable);

    @Query("SELECT i " +
            "FROM Inquiry i " +
            "JOIN FETCH i.user " +
            "WHERE i.isDeleted = false")
    Page<Inquiry> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Inquiry> findByIdAndIsDeletedFalse(Long inquiryId);

}
