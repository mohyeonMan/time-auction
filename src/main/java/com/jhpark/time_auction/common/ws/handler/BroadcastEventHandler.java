package com.jhpark.time_auction.common.ws.handler;

import java.util.Set;

import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;

public interface BroadcastEventHandler {
    
    Set<ServerEventType> supports();

    /**
     * 메시지를 실제로 처리합니다.
     * @param channel 메시지가 수신된 채널
     * @param message 수신된 메시지
     */
    void handle(String channel, ServerEvent message);
}