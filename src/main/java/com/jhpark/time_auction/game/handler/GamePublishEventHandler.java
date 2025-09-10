package com.jhpark.time_auction.game.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhpark.time_auction.common.ws.handler.PublishEventHandler;
import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent;
import com.jhpark.time_auction.common.ws.model.in.ClientEventType;
import com.jhpark.time_auction.common.ws.model.out.ServerEvent;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GamePublishEventHandler implements PublishEventHandler {

    private final GameService gameService;
    private final RoomService roomService;
    private final MessagePublisher<ServerEvent> publisher;

    @Override
    public Set<ClientEventType> supports() {
        return Set.of(ClientEventType.GAME_START, ClientEventType.ROUND_IN, ClientEventType.ROUND_OUT);
    }

    @Override
    public void handle(WebSocketSession session, ClientEvent event) {
        String userId = session.getAttributes().get("HTTP_SESSION_ID").toString();

        switch (event.getType()) {
            case GAME_START: {
                ClientEvent.GameStartEvent gameStartEvent = (ClientEvent.GameStartEvent) event;
                Game game = gameService.startGame(gameStartEvent.getRoomId());
                // The service internally starts the first round, which will trigger its own broadcast events.
                publisher.publish("ws:room:" + game.getRoomId(), new ServerEvent.GameStartBroadcastEvent(game, LocalDateTime.now()));
                break;
            }
            case ROUND_IN: {
                ClientEvent.RoundInEvent roundInEvent = (ClientEvent.RoundInEvent) event;
                roomService.setParticipation(roundInEvent.getGameId(), userId, true);
                // No broadcast needed, this is a private choice. A confirm event could be sent.
                break;
            }
            case ROUND_OUT: {
                ClientEvent.RoundOutEvent roundOutEvent = (ClientEvent.RoundOutEvent) event;
                roomService.setParticipation(roundOutEvent.getGameId(), userId, false);
                break;
            }
            default:
                log.warn("Unsupported event type by GamePublishEventHandler: {}", event.getType());
        }
    }
}
