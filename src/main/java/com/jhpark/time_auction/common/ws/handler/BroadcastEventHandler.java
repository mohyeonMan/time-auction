package com.jhpark.time_auction.common.ws.handler;

import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

public interface BroadcastEventHandler {
    /**
     * 이 핸들러가 해당 채널을 지원하는지 확인합니다.
     * @param channel 메시지가 수신된 채널
     * @return 지원 여부
     */
    boolean supports(String channel);

    /**
     * 메시지를 실제로 처리합니다.
     * @param channel 메시지가 수신된 채널
     * @param message 수신된 메시지
     */
    void handle(String channel, ServerEvent message);
}