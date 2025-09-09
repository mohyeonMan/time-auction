package com.jhpark.time_auction.common.ws.model.in;

public enum ClientEventType {
    PING,       // 핑
    JOIN,       // 방 참여
    LEAVE,      // 방 나감
    READY,      // 준비
    NOT_READY,  // 준비 해제
    GAME_START, // 게임 시작
    GAME_END,   // 게임 종료
    ROUND_IN,   // 라운드 참여
    ROUND_OUT,  // 라운드 미참여
    TIME_START, // 시간 집계 시작
    TIME_END,   // 시간 집계 종료
    CHAT        // 채팅
}