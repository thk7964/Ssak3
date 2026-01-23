package com.example.ssak3.domain.inquirychat.repository;

import com.example.ssak3.domain.inquirychat.entity.InquiryChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryChatMessageRepository extends JpaRepository<InquiryChatMessage, Long> {
}
