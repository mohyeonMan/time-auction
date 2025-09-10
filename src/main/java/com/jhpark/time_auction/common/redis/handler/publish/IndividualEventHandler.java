package com.jhpark.time_auction.common.redis.handler.publish;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.jhpark.time_auction.common.ws.handler.GameManager;
import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
import com.jhpark.time_auction.common.ws.handler.SessionManager;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.RoundInEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.RoundOutEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.TimeEndEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.TimeStartEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndividualEventHandler implements PublishEventHandler {
    
    private final SessionManager sessionManager;
    private final GameManager gameManager;
    private final static String NODE_PREFIX = "ws:node:";
    private final static String SESSION_INFIX = ":session:";
    

    @Override
    public Set<ClientEventType> supports() {
        return Set.of(
            ClientEventType.PING,
            ClientEventType.ROUND_IN, 
            ClientEventType.ROUND_OUT,
            ClientEventType.TIME_START,
            ClientEventType.TIME_END
        );
    }

    @Override
    public void handle(WebSocketSession session, ClientEvent event) {

        final ClientEventType type = event.getType();
        
        switch (type) {
            case PING -> sessionManager.renewExpiration(session.getId());
            case ROUND_IN -> handleRoundInEvent(session, event);
            case ROUND_OUT -> handleRoundOutEvent(session, event);
            case TIME_START -> handleTimeStartEvent(session, event);
            case TIME_END -> handleTimeEndEvent(session, event);

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
        gameManager.roundIn(roundId, sessionManager.getSessionKey(session.getId()));
        
        sessionManager.sendToSession(session, 
            new ServerEvent.RoundInConfirmEvent(gameId, roundId, true, sentAt)
        );

    };

    private void handleRoundOutEvent(WebSocketSession session, ClientEvent event){
        final RoundOutEvent roundOutEvent = (RoundOutEvent) event;
        final String gameId = roundOutEvent.getGameId();
        final String roundId = roundOutEvent.getRoundId();
        final LocalDateTime sentAt = roundOutEvent.getSentAt();
        
        gameManager.roundOut(roundId, sessionManager.getSessionKey(session.getId()));

        sessionManager.sendToSession(session, 
            new ServerEvent.RoundOutConfirmEvent(gameId, roundId, true, sentAt)
        );
    };

    private void handleTimeStartEvent(WebSocketSession session, ClientEvent event){
        final TimeStartEvent timeStartEvent = (TimeStartEvent) event;
        final String sessionKey = sessionManager.getSessionKey(session.getId());
        final String roundId = timeStartEvent.getRoundId();
        final LocalDateTime sentAt = timeStartEvent.getSentAt();

        gameManager.timeStart(roundId, sessionKey, sentAt);

        sessionManager.sendToSession(session, 
            new ServerEvent.TimeStartConfirmEvent(sessionKey, roundId, true, sentAt)
        );

    };

    private void handleTimeEndEvent(WebSocketSession session, ClientEvent event){
        final TimeEndEvent timeStartEvent = (TimeEndEvent) event;
        final String sessionKey = sessionManager.getSessionKey(session.getId());
        final String roundId = timeStartEvent.getRoundId();
        final LocalDateTime sentAt = timeStartEvent.getSentAt();

        gameManager.timeEnd(roundId, sessionKey, sentAt);
        
        sessionManager.sendToSession(session, 
            new ServerEvent.TimeStartConfirmEvent(sessionKey, roundId, true, sentAt)
        );

    };


}
