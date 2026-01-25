package com.example.ssak3.domain.inquirychat.model.response;

import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryChatListGetResponse {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final Long adminId;
    private final ChatRoomStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryChatListGetResponse from(InquiryChatRoom room) {
        return new InquiryChatListGetResponse(
                room.getId(),
                room.getUser().getId(),
                room.getUser().getName(),
                room.getAdmin() != null ? room.getAdmin().getId() : null,
                room.getStatus(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
