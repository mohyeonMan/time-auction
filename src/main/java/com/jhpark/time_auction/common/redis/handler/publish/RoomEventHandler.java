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
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.NotReadyEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.ReadyEvent;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomEventHandler implements PublishEventHandler {

    private final SessionManager sessionManager;
    private final MessagePublisher<ServerEvent> messagePublisher;
    private final static String ROOM_PREFIX = "ws:room:";
    
    @Override
    public Set<ClientEventType> supports() {
        return Set.of(
            ClientEventType.CHAT,
            ClientEventType.GAME_START,
            ClientEventType.READY,
            ClientEventType.NOT_READY
        );
    }

    @Override
    public void handle(WebSocketSession session, ClientEvent event) {

        final ClientEventType type = event.getType();
        
        switch (type) {
            case CHAT -> handleChatEvent(session, event);
            case GAME_START -> handleGameStartEvent(session, event);
            case GAME_END -> handleGameEndEvent(session, event);
            case READY -> handleReadyEvent(session, event);
            case NOT_READY -> handleNotReadyEvent(session, event);

            default -> throw new RuntimeException();
        }

    }

    public String getDestination(String roomId){
        return ROOM_PREFIX + roomId;
    }

    private void handleChatEvent(WebSocketSession session, ClientEvent event){
        final ChatEvent chatEvent = (ChatEvent) event;
        final String sessionKey = sessionManager.getSessionKey(session.getId());
        final String message = chatEvent.getMessage();
        final String roomId = chatEvent.getRoomId();
        final LocalDateTime sentAt = chatEvent.getSentAt();

        final String dest = getDestination(roomId);

        ServerEvent.ChatEvent serverChatEvent = 
            new ServerEvent.ChatEvent(
                    sessionKey, 
                    message, 
                    roomId, 
                    sentAt
                );
        messagePublisher.publish(dest, serverChatEvent);
    };


    private void handleReadyEvent(WebSocketSession session, ClientEvent event){
        ReadyEvent readyEvent = (ReadyEvent) event;
        
    };

    private void handleNotReadyEvent(WebSocketSession session, ClientEvent event){
        NotReadyEvent notReadyEvent = (NotReadyEvent) event;
    };

    private void handleGameStartEvent(WebSocketSession session, ClientEvent event){
    }
    private void handleGameEndEvent(WebSocketSession session, ClientEvent event){
    }
}
