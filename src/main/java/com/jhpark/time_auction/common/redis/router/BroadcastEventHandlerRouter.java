package com.jhpark.time_auction.common.redis.router;

import org.springframework.stereotype.Component;

import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.BroadcastEventHandler;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class BroadcastEventHandlerRouter {
    
    private final Map<ServerEventType, BroadcastEventHandler> handlerMap;

    public BroadcastEventHandlerRouter(List<BroadcastEventHandler> handlers) {
        this.handlerMap = new HashMap<>();

        for (BroadcastEventHandler handler : handlers) {
            Set<ServerEventType> supportedTypes = handler.supports();
            
            for (ServerEventType type : supportedTypes) {
                handlerMap.put(type, handler);
            }
        }
    }

    public void route(String channel, ServerEvent event) {
        ServerEventType type = event.getType();
        BroadcastEventHandler handler = handlerMap.get(type);

        if (handler != null) {
            handler.handle(channel, event);
        } else {
            log.error("EVENT NOT SUPPORTED : {}", type);
        }
    }
}