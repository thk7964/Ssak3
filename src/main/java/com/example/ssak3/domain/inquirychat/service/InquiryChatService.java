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
import com.example.ssak3.domain.inquirychat.model.response.InquiryChatStatusUpdateResponse;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatMessageRepository;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatRoomRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryChatService {

    private final InquiryChatRoomRepository roomRepository;
    private final InquiryChatMessageRepository messageRepository;
    private final UserRepository userRepository;


    /**
     * 문의 채팅방 생성
     */
    @Transactional
    public InquiryChatCreateResponse createChatRoom(Long userId) {

        Optional<InquiryChatRoom> activeRoom = roomRepository.findActiveRoomByUserId(userId);

        // 활성화된 채팅방이 있다면 기존 채팅방으로 연결
        if (activeRoom.isPresent()) {
            return InquiryChatCreateResponse.from(activeRoom.get());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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

        // 회원일 경우 본인의 채팅방만 조회 가능
        if (senderRole == UserRole.USER) {
            if (!foundRoom.isDeleted() && !foundRoom.getUser().getId().equals(senderId)) {
                throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
            }
        // 관리자일 경우 자신에게 배정된 채팅이 아닐 경우 조회 불가
        } else if (senderRole == UserRole.ADMIN) {
            if(!foundRoom.isDeleted() && foundRoom.getAdmin() != null && !foundRoom.getAdmin().getId().equals(senderId)) {
                throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
            }
        }

        return messageRepository.findAllByRoomId(roomId)
                .stream()
                .map(InquiryChatMessageListGetResponse::from)
                .toList();
    }


    /**
     * 문의 채팅방 종료
     */
    @Transactional
    public InquiryChatStatusUpdateResponse updateRoomStatus(Long userId, Long roomId) {

        InquiryChatRoom foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 이미 종료된 채팅방인지 확인
        if (foundRoom.getStatus() == ChatRoomStatus.COMPLETED) {
            throw new CustomException(ErrorCode.INQUIRY_CHAT_ALREADY_COMPLETED);
        }

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 관리자일 경우 어떤 채팅방이든 종료 가능
        if (foundUser.getRole() == UserRole.ADMIN) {
            foundRoom.chatComplete();
        }
        // 회원일 경우 본인의 채팅방만 종료 가능
        else {
            if (foundRoom.getUser().getId().equals(userId)) {
                foundRoom.chatComplete();
            } else {
                throw new CustomException(ErrorCode.CHAT_ROOM_COMPLETE_ACCESS_DENIED);
            }
        }

        return InquiryChatStatusUpdateResponse.from(foundRoom);
    }

    /**
     * 문의 채팅 메시지 비동기 저장
     */
    @Async("chatExecutor")  // 별도 스레드에서 실행
    @Transactional
    public void saveMessageAsync(ChatMessageRequest request, Long userId, String role) {  // 비동기 작업 결과를 나타냄
        try {
            InquiryChatRoom foundRoom = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

            // 이미 종료된 채팅방인지 확인
            if (foundRoom.getStatus() == ChatRoomStatus.COMPLETED) {
                log.warn("종료된 채팅방에 메시지 저장 시도 - roomId: {}, userId: {}", request.getRoomId(), userId);
                throw new CustomException(ErrorCode.INQUIRY_CHAT_ALREADY_COMPLETED);
            }

            User foundUser = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            InquiryChatMessage message = new InquiryChatMessage(
                    foundRoom,
                    foundUser,
                    UserRole.valueOf(role),
                    request.getType(),
                    request.getContent()
            );

            messageRepository.save(message);

        } catch (Exception e) {
            log.error("비동기 메시지 저장 실패 - roomId: {}, userId: {}, error: {}",
                    request.getRoomId(), userId, e.getMessage(), e);
        }
    }
}
