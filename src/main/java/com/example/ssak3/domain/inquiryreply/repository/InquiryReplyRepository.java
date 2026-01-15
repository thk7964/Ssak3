package com.example.ssak3.domain.inquiryreply.repository;

import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {

    boolean existsByInquiryId(Long inquiryId);
}
