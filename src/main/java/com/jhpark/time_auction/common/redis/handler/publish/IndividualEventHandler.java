package com.jhpark.time_auction.common.redis.handler.publish;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.RoundInEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.RoundOutEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndividualEventHandler implements PublishEventHandler {
    
    private final SessionManager sessionManager;
    private final MessagePublisher<ServerEvent> messagePublisher;
    private final static String NODE_PREFIX = "ws:node:";
    private final static String SESSION_INFIX = ":session:";
    

    @Override
    public Set<ClientEventType> supports() {
        return Set.of(
            ClientEventType.ROUND_IN, 
            ClientEventType.ROUND_OUT
        );
    }

    @Override
    public void handle(WebSocketSession session, ClientEvent event) {

        final ClientEventType type = event.getType();
        
        switch (type) {
            case ROUND_IN -> handleRoundInEvent(session, event);
            case ROUND_OUT -> handleRoundOutEvent(session, event);
            case TIME_START -> handleRoundInEvent(session, event);
            case TIME_END -> handleRoundInEvent(session, event);

            default -> throw new RuntimeException();
        }

    }

    public String getDestination(String nodeId, String sessionKey){
        return NODE_PREFIX+nodeId+SESSION_INFIX+sessionKey;
    }

    private void handleRoundInEvent(WebSocketSession session, ClientEvent event){
        final RoundInEvent roundInEvent = (RoundInEvent) event;
        final String gameId = roundInEvent.getGameId();
        final String roundId = roundInEvent.getRoundId();
        final LocalDateTime sentAt = roundInEvent.getSentAt();

        // 해당 라운드 경매 참여처리
        
        sessionManager.sendToSession(session, 
            new ServerEvent.RoundInConfirmEvent(gameId, roundId, true, sentAt)
        );

    };

    private void handleRoundOutEvent(WebSocketSession session, ClientEvent event){
        RoundOutEvent roundOutEvent = (RoundOutEvent) event;
        final String gameId = roundOutEvent.getGameId();
        final String roundId = roundOutEvent.getRoundId();
        final LocalDateTime sentAt = roundOutEvent.getSentAt();
        
        //해당 라운드 경매 미참여처리

        sessionManager.sendToSession(session, 
            new ServerEvent.RoundOutConfirmEvent(gameId, roundId, true, sentAt)
        );
    };
}
