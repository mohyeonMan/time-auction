package com.jhpark.time_auction.common.redis.router;

import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class PublishMessageRouter {

    private final Map<ClientEventType, PublishEventHandler> handlerMap;

    public PublishMessageRouter(List<PublishEventHandler> handlers) {
        this.handlerMap = new HashMap<>();

        for (PublishEventHandler handler : handlers) {
            Set<ClientEventType> supportedTypes = handler.supports();
            
            for (ClientEventType type : supportedTypes) {
                handlerMap.put(type, handler);
            }
        }
    }

    /**
     * 클라이언트 메시지를 받아 메시지 유형에 따라 적절한 핸들러로 라우팅합니다.
     *
     * @param session 현재 웹소켓 세션
     * @param message 클라이언트로부터 수신한 BaseMessage 객체
     */
    public void route(WebSocketSession session, ClientEvent event) {
        ClientEventType type = event.getType();
        PublishEventHandler handler = handlerMap.get(type);

        if (handler != null) {
            handler.handle(session, event);
        } else {
            log.error("EVENT NOT SUPPORTED : {}", type);
        }
    }
}