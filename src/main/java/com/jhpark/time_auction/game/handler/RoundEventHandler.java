package com.jhpark.time_auction.game.handler;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.GameEntry;
import com.jhpark.time_auction.game.model.RoundParticipation;
import com.jhpark.time_auction.game.model.RoundStatus;
import com.jhpark.time_auction.game.service.GameEntryService;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.game.service.RoundParticipationService;
import com.jhpark.time_auction.game.service.RoundService;

import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoundEventHandler {
    private final GameService gameService;
    private final GameEntryService gameEntryService;
    private final RoundService roundService;
    private final RoundParticipationService roundParticipationService;
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleRoundIn(
        String cid,
        long sentAt,
        String roundId,
        String gameEntryId
    ) {

        GameEntry gameEntry = gameEntryService.getGameEntry(gameEntryId);
        Game game = gameService.getGame(gameEntry.getGameId());
        String roomId = game.getRoomId();

        RoundParticipation participation = roundParticipationService.roundIn(roundId, gameEntryId);
        log.info("참가 결정 : gameEntryId : {}, roundId : {}");

        publisher.publishToUser(gameEntry.getSessionId(), Dest.userPersonal(roomId),
                ServerEvent.builder()
                        .type(ServerEventType.ROUND_IN_CONFIRM)
                        .cid(cid)
                        .clientAt(sentAt)
                        .payload(participation)
                        .build());
                        
        startBidSignal(cid, sentAt, roomId, game.getGameEntryIds(), roundId);

        return Ack.ok(cid, participation.getId());
    }


    public Ack<?> handleRoundOut(
        String cid,
        long sentAt,
        String roundId,
        String gameEntryId
    ) {
        GameEntry gameEntry = gameEntryService.getGameEntry(gameEntryId);
        Game game = gameService.getGame(gameEntry.getGameId());
        String roomId = game.getRoomId();

        RoundParticipation participation = roundParticipationService.roundOut(roundId, gameEntryId);
        log.info("미참가 결정 : gameEntryId : {}, roundId : {}");

        publisher.publishToUser(gameEntry.getSessionId(), Dest.userPersonal(roomId),
                ServerEvent.builder()
                        .type(ServerEventType.ROUND_IN_CONFIRM)
                        .cid(cid)
                        .clientAt(sentAt)
                        .payload(participation)
                        .build());
                        
        startBidSignal(cid, sentAt, roomId, game.getGameEntryCounts(), roundId);

        return Ack.ok(cid, participation.getId());
    }

    public void startBidSignal(
        String cid, 
        long sentAt, 
        String roomId,
        int gameEntryCounts, 
        String roundId
    ){

        Set<String> choosenEntryIds = roundParticipationService.getChoosenGameEntryIds(roundId);

        if (choosenEntryIds.size() == gameEntryCounts) {
            log.info("전원 참가 여부 선택 : roundId : {}");

            roundService.setRoundStatus(roundId, RoundStatus.RUNNING);

            publisher.publish(Dest.roomEvent(roomId), 
                ServerEvent.builder()
                    .type(ServerEventType.ROUND_START_BROADCAST)
                    .cid(cid)
                    .clientAt(sentAt)
                    .payload(roundId)
                    .build());
            
        }
    }
}
