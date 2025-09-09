package com.jhpark.time_auction.common.redis.handler.publish;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
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
            ClientEventType.ROUND_IN, 
            ClientEventType.ROUND_OUT,
            ClientEventType.READY,
            ClientEventType.ROUND_OUT
        );
    }

    @Override
    public void handle(WebSocketSession session, ClientEvent event) {
        sessionManager.renewExpiration(session.getId());

        sessionManager.sendToSession(session, new ServerEvent.PongEvent(event.getSentAt()));

        log.info("PONG : {}", session.getId());
        
    }

}
