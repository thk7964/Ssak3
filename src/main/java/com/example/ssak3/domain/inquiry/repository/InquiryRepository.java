package com.example.ssak3.domain.inquiry.repository;

import com.example.ssak3.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 회원용 조회 조회
    Page<Inquiry> findAllByIsDeletedFalseAndUserId(Long userId, Pageable pageable);

    // 관리자용 문의 조회
    Page<Inquiry> findAllByIsDeletedFalse(Pageable pageable);


}
