package com.jhpark.time_auction.game.handler;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.GameInfo;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.service.RoomService;
import com.jhpark.time_auction.user.model.GameEntry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameEventHandler {
    private final GameService gameService;
    private final RoomService roomService;
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleStartGame(
            String cid,
            long sentAt,
            String roomId,
            Integer totalRound,
            Integer totalTime,
            String sessionId) {

        // room.masterId와 sessionId 를 이용해 시작 검증.

        // 준비한 roomEntry들.
        List<RoomEntry> readyEntriess = roomService.getReadyEntries(roomId);
        List<String> entryIds = readyEntriess.stream().map(RoomEntry::getId).collect(Collectors.toList());

        // 게임 생성
        GameInfo newGameInfo = gameService.startGame(roomId, totalRound, totalTime, entryIds);
        Game newGame = newGameInfo.getGame();
        List<GameEntry> newGameEntries = newGameInfo.getGameEntryId();

        for (GameEntry gameEntry : newGameEntries) {

            publisher.publishToUser(gameEntry.getSessionId(), Dest.userPersonal(roomId),
                    ServerEvent.builder()
                            .type(ServerEventType.GAME_START_BROADCAST)
                            .cid(cid)
                            .clientAt(sentAt)
                            .payload(Map.of(
                                    "game", newGame,
                                    "gameEntry", gameEntry))
                            .build());

        }

        return Ack.ok(cid, newGame.getId());
    }
}