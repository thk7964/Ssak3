package com.example.ssak3.domain.inquiryreply.repository;

import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {

    Optional<InquiryReply> findByInquiryIdAndIsDeletedFalse(Long inquiryId);
}
