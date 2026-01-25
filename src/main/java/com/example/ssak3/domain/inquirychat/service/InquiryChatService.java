package com.example.ssak3.domain.inquirychat.service;

import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatMessage;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import com.example.ssak3.domain.inquirychat.model.response.InquiryChatCreateResponse;
import com.example.ssak3.domain.inquirychat.model.response.InquiryChatMessageListGetResponse;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatMessageRepository;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatRoomRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
    public InquiryChatCreateResponse createChatRoom(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        InquiryChatRoom room = new InquiryChatRoom(
                user,
                null,
                ChatRoomStatus.WAITING
        );

        InquiryChatRoom savedRoom = roomRepository.save(room);

        return InquiryChatCreateResponse.from(savedRoom);
    }


    /**
     * 문의 채팅 메시지 조회
     */
    @Transactional(readOnly = true)
    public List<InquiryChatMessageListGetResponse> getChatHistory(Long roomId, Long senderId, UserRole senderRole) {

        InquiryChatRoom foundRoom= roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 회원과 관리자 기준 분리
        if (senderRole == UserRole.USER) {  // 회원일 경우 본인 방이 아닐 경우 조회 불가
            if (!foundRoom.isDeleted() && !foundRoom.getUser().getId().equals(senderId)) {
                throw new CustomException(ErrorCode.ACCESS_DENIED_CHAT_ROOM);
            }
        } else if (senderRole == UserRole.ADMIN) {  // 관리자일 경우 자신에게 배정된 채팅이 아닐 경우 조회 불가
            if(!foundRoom.isDeleted() && foundRoom.getAdmin() != null && !foundRoom.getAdmin().getId().equals(senderId)) {
                throw new CustomException(ErrorCode.ACCESS_DENIED_CHAT_ROOM);
            }
        }

        return messageRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(InquiryChatMessageListGetResponse::from)
                .toList();
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
