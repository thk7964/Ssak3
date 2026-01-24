package com.example.ssak3.domain.inquirychat.service;

import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatMessage;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import com.example.ssak3.domain.inquirychat.model.response.ChatRoomCreateResponse;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatMessageRepository;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatRoomRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryChatService {

    private final InquiryChatRoomRepository roomRepository;
    private final InquiryChatMessageRepository messageRepository;
    private final UserRepository userRepository;


    /**
     * 문의 채팅방 생성
     */
    @Transactional
    public ChatRoomCreateResponse createChatRoom(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        InquiryChatRoom room = new InquiryChatRoom(
                user,
                null,
                ChatRoomStatus.OPEN
        );

        InquiryChatRoom savedRoom = roomRepository.save(room);

        return ChatRoomCreateResponse.from(savedRoom);
    }


    /**
     * 문의 채팅방 연결
     */
    @Transactional
    public void saveMessage(ChatMessageRequest request) {

        InquiryChatRoom room = roomRepository.findById(Long.valueOf(request.getRoomId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        User user = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        InquiryChatMessage message = new InquiryChatMessage(
                room,
                user,
                request.getSenderRole(),
                request.getType(),
                request.getContent()
        );

        messageRepository.save(message);
    }

}
