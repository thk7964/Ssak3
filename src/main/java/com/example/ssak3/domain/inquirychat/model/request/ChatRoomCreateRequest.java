package com.example.ssak3.domain.inquirychat.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private Long userId;    // 유저가 문의 상담을 요청하므로 userId 필요
}
