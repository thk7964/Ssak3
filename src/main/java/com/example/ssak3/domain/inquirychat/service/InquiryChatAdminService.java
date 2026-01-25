package com.example.ssak3.domain.inquirychat.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import com.example.ssak3.domain.inquirychat.model.response.InquiryChatAcceptResponse;
import com.example.ssak3.domain.inquirychat.model.response.InquiryChatListGetResponse;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatMessageRepository;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatRoomRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryChatAdminService {

    private final InquiryChatRoomRepository inquiryChatRoomRepository;
    private final InquiryChatMessageRepository messageRepository;
    private final UserRepository userRepository;

    /**
     * 관리자 문의 채팅방 참여
     */

    @Transactional
    public InquiryChatAcceptResponse acceptChat(Long adminId, Long roomId) {

        InquiryChatRoom foundRoom = inquiryChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User foundAdmin = userRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        foundRoom.assignAdmin(foundAdmin);

        return InquiryChatAcceptResponse.from(foundRoom);

    }

    /**
     * 관리자 문의 채팅방 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<InquiryChatListGetResponse> getChatRoom(Long adminId, Pageable pageable) {

        Page<InquiryChatListGetResponse> inquiryChatListPage = inquiryChatRoomRepository
                .findAllByAdminIdOrWaiting(adminId, pageable)
                .map(InquiryChatListGetResponse::from);

        return PageResponse.from(inquiryChatListPage);

    }


}
