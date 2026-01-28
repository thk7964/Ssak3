package com.example.ssak3.domain.inquirychat.model.response;

import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryChatStatusUpdateResponse {
    private final Long id;
    private final Long userId;
    private final Long adminId;
    private final ChatRoomStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryChatStatusUpdateResponse from(InquiryChatRoom room) {
        return new InquiryChatStatusUpdateResponse(
                room.getId(),
                room.getUser().getId(),
                room.getAdmin() != null ? room.getAdmin().getId() : null,
                room.getStatus(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
