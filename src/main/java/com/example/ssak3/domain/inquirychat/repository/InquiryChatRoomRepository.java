package com.example.ssak3.domain.inquirychat.repository;

import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InquiryChatRoomRepository extends JpaRepository<InquiryChatRoom, Long> {

    // 현재 관리자의 모든 채팅방 Id와 아직 배정받지 않은 채팅방(WAITING) 조회
    @Query("SELECT r FROM InquiryChatRoom r " +
            "JOIN FETCH r.user " +
            "LEFT JOIN FETCH r.admin " +
            "WHERE r.status = 'WAITING' " +
            "OR (r.admin.id = :adminId AND r.status = 'ONGOING') " +
            "ORDER BY " +
            "CASE r.status WHEN 'ONGOING' THEN 1 " + // 채팅 진행 중인 것을 1순위
            "               WHEN 'WAITING' THEN 2 " + // 채팅 대기 중인 것을 2순위
            "               ELSE 3 END ASC, " +
            "r.updatedAt DESC") // 그 안에서는 최신 수정순
    Page<InquiryChatRoom> findAllByAdminIdOrWaiting(@Param("adminId") Long adminId, Pageable pageable);

    // 활성화된 채팅방(WAITING, ONGOING) 조회
    @Query("SELECT r FROM InquiryChatRoom r " +
            "WHERE r.user.id = :userId " +
            "AND (r.status = 'WAITING' OR r.status = 'ONGOING') " +
            "AND r.isDeleted = false")
    Optional<InquiryChatRoom> findActiveRoomByUserId(@Param("userId") Long userId);

}
