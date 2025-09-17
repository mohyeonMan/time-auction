package com.jhpark.time_auction.game.handler;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.GameInfo;
import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.game.service.RoundService;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.service.RoomEntryService;
import com.jhpark.time_auction.room.service.RoomService;
import com.jhpark.time_auction.user.model.GameEntry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameEventHandler {
    private final GameService gameService;
    private final RoundService roundService;
    private final RoomService roomService;
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleStartGame(
        String cid,
        long sentAt,
        String roomId,
        Integer totalRound,
        Integer totalTime,
        String sessionId
    ) {

        Room room = roomService.getRoomByRoomId(roomId);
        // room.masterId와 sessionId 를 이용해 시작 검증.

        // 게임 생성
        GameInfo newGameInfo = gameService.startGame(roomId, totalRound, totalTime);
        Game newGame = newGameInfo.getGame();
        List<GameEntry> newGameEntries = newGameInfo.getGameEntryId();

        //방 참가자들 모두에게 gameEntry 정보를 알려줌.
        for (GameEntry gameEntry : newGameEntries) {
            publisher.publishToUser(
                gameEntry.getSessionId(), 
                Dest.userPersonal(roomId),
                ServerEvent.builder()
                    .type(ServerEventType.GAME_START_CONFIRM)
                    .cid(cid)
                    .clientAt(sentAt)
                    .payload(gameEntry)
                    .build()
            );
        }

        // 방에 게임시작 알림.
        publisher.publish(Dest.roomState(roomId), 
            ServerEvent.builder()
                    .type(ServerEventType.GAME_START_BROADCAST)
                    .cid(cid)
                    .clientAt(sentAt)
                    .payload(newGame)
                    .build());
        
        Round nextRound = roundService.readyNextRound(newGame.getId());

        publisher.publish(Dest.roomEvent(roomId),
            ServerEvent.builder()
                .type(ServerEventType.NEXT_ROUND_READY)
                .cid(cid)
                .clientAt(sentAt)
                .payload(nextRound)
                .build());

        
        return Ack.ok(cid, newGame.getId());
    }

    // public Ack<?> handleNextRound(
    //     String cid,
    //     long sentAt,
    //     String gameId,
    //     String sessionId
    // ) {
    //     Game game = gameService.getGame(gameId);


    //     publisher.publish(Dest.roomEvent(game.getRoomId()),
    //         ServerEvent.builder()
    //             .type(ServerEventType.NEXT_ROUND_READY)
    //             .cid(cid)
    //             .clientAt(sentAt)
    //             .payload(nextRound)
    //             .build()
    //     );

    //     return Ack.ok(cid, nextRound.getId());

    // }




    
}