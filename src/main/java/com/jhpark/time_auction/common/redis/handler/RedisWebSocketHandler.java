package com.jhpark.time_auction.common.redis.handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhpark.time_auction.common.redis.router.PublishMessageRouter;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Slf4j
@Component
public class RedisWebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper;
    private final PublishMessageRouter publishMessageRouter;

    public RedisWebSocketHandler(
            RedisSessionManager sessionManager,
            ObjectMapper objectMapper,
            PublishMessageRouter publishMessageRouter) {
        this.sessionManager = sessionManager;
        this.objectMapper = objectMapper;
        this.publishMessageRouter = publishMessageRouter;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        sessionManager.addSession(session);
        
        log.info("WebSocket connection established. Session ID: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Redis에서 세션 정보 제거
        sessionManager.removeSession(session);
        
        log.info("WebSocket connection closed. Session ID: " + session.getId() + ", Reason: " + status.getReason() + " -");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        ClientEvent event = objectMapper.readValue(textMessage.getPayload(), ClientEvent.class);
        publishMessageRouter.route(session, event);
    }

}