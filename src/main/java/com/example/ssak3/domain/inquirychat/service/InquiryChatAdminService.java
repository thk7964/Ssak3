package com.example.ssak3.domain.inquirychat.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import com.example.ssak3.domain.inquirychat.model.response.ChatAcceptResponse;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatMessageRepository;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatRoomRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryChatAdminService {

    private final InquiryChatRoomRepository roomRepository;
    private final InquiryChatMessageRepository messageRepository;
    private final UserRepository userRepository;

    /**
     * 관리자 문의 채팅방 참여
     */
    @Transactional
    public ChatAcceptResponse acceptChat(Long adminId, Long roomId) {

        InquiryChatRoom foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User foundAdmin = userRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        foundRoom.assignAdmin(foundAdmin);

        return ChatAcceptResponse.from(foundRoom);

    }

}
