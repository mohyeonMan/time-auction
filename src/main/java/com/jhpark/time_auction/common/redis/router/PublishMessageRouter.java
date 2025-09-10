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