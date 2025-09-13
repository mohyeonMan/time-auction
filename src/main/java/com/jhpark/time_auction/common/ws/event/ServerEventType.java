package com.jhpark.time_auction.common.ws.event;

public enum ServerEventType {
    PONG,               // 핑
    CREATE_ROOM,
    GET_ROOM,
    JOIN_CONFIRM,       // 방 참여 확인
    LEAVE_CONFIRM,      // 방 나감 확인
    READY_CONFIRM,      // 준비 확인
    NOT_READY_CONFIRM,  // 준비 해제 확인
    GAME_START_CONFIRM, // 게임 시작 확인
    GAME_END_CONFIRM,   // 게임 종료 확인
    ROUND_IN_CONFIRM,   // 라운드 참여 확인
    ROUND_OUT_CONFIRM,  // 라운드 미참여 확인
    TIME_START_CONFIRM, // 시간 집계 시작 확인
    TIME_END_CONFIRM,   // 시간 집계 종료 확인
    CHAT,               // 채팅
    NOTICE_MESSAGE,     // 공지 메시지
    PHASE_CHANGED,      // 페이즈 변경알림

    // Broadcast event types
    USER_JOINED_BROADCAST,
    USER_LEFT_BROADCAST,
    READY_BROADCAST,
    GAME_START_BROADCAST,
    ROUND_START_BROADCAST,
    ROUND_END_BROADCAST,
    GAME_END_BROADCAST
}
