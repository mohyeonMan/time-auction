package com.jhpark.time_auction.common.redis.handler.publish;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.ChatEvent;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatEventHandler implements PublishEventHandler {

    private final SessionManager sessionManager;
    private final MessagePublisher<ServerEvent> messagePublisher;
    private final static String CHAT_PREFIX = "ws:room:";

    @Override
    public Set<ClientEventType> supports() {
        return Set.of();
    }

    @Override
    public void handle(WebSocketSession session, ClientEvent event) {
        final ChatEvent chatEvent = (ChatEvent) event;

        final String sender = sessionManager.getSessionKey(session.getId());
        final String roomId = chatEvent.getRoomId();
        final String message = chatEvent.getMessage();
        final LocalDateTime sentAt = chatEvent.getSentAt();

        final String dest = getChatDestination(chatEvent.getRoomId());
        
        messagePublisher.publish(dest, new ServerEvent.ChatEvent(sender, message, roomId, sentAt));
        
        log.info("SEND CHAT MESSAGE : {} {} {}", sender, message, roomId);
    }

    private String getChatDestination(String roomId){
        return CHAT_PREFIX + roomId;
    }
    
}
