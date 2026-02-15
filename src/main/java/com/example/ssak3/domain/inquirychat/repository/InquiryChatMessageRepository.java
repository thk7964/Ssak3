package com.example.ssak3.domain.inquirychat.repository;

import com.example.ssak3.domain.inquirychat.entity.InquiryChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryChatMessageRepository extends JpaRepository<InquiryChatMessage, Long> {

    @Query("SELECT m " +
            "FROM InquiryChatMessage m " +
            "JOIN FETCH m.sender WHERE m.room.id = :roomId " +
            "ORDER BY m.createdAt ASC")
    List<InquiryChatMessage> findAllByRoomId(@Param("roomId") Long roomId);

}
