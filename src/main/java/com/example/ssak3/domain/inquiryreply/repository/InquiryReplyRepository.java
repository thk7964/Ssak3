package com.example.ssak3.domain.inquiryreply.repository;

import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import com.example.ssak3.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {

    Page<InquiryReply> findAllByIsDeletedFalse(Pageable pageable);

}
