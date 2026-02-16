package com.example.ssak3.domain.inquirychat.repository;

import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InquiryChatRoomRepository extends JpaRepository<InquiryChatRoom, Long> {

    @Query("SELECT r FROM InquiryChatRoom r " +
            "JOIN FETCH r.user " +
            "LEFT JOIN FETCH r.admin " +
            "WHERE r.status = 'WAITING' " +
            "OR (r.admin.id = :adminId AND r.status = 'ONGOING') " +
            "OR (r.admin.id = :adminId AND r.status = 'COMPLETED') " +
            "ORDER BY " +
            "CASE r.status WHEN 'ONGOING' THEN 1 " +
            "               WHEN 'WAITING' THEN 2 " +
            "               ELSE 3 END ASC, " +
            "r.updatedAt DESC")
    Page<InquiryChatRoom> findAllByAdminIdOrWaiting(@Param("adminId") Long adminId, Pageable pageable);

    @Query("SELECT r FROM InquiryChatRoom r " +
            "WHERE r.user.id = :userId " +
            "AND (r.status = 'WAITING' OR r.status = 'ONGOING') " +
            "AND r.isDeleted = false")
    Optional<InquiryChatRoom> findActiveRoomByUserId(@Param("userId") Long userId);

}
