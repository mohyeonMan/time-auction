package com.jhpark.time_auction.common.ws.handler.publish;

import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

public interface MessagePublisher<T extends ServerEvent> {

    /**
     * 특정 목적지로 메시지를 발행(publish)합니다.
     * * @param destination 메시지를 보낼 채널 또는 목적지 (예: Redis 채널)
     * @param message 발행할 메시지 객체
     */
    void publish(String destination, T message);

}