package com.jhpark.time_auction.common.ws.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SimpleHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        // 받은 메시지 그대로 돌려줌 (echo)
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }
}