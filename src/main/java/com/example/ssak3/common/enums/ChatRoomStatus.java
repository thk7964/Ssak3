package com.example.ssak3.common.enums;

public enum ChatRoomStatus {
    WAITING,  // 문의 생성(관리자 배정 전)
    CANCELED,  // 관리자 배정 전 취소
    ONGOING,  // 문의 중
    COMPLETED  // 문의 완료
}