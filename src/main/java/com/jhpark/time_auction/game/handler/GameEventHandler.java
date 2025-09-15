package com.jhpark.time_auction.game.handler;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameEventHandler {
    private final GameService gameService;
    private final RoomService roomService; // 게임 시작 시 참가자 정보를 가져오기 위해 주입
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleStartGame(String cid, long sentAt, String roomId, String roomEntryId) {
        // 방에 있는 모든 Entry를 가져와 ID 목록을 만든다.
        List<String> roomEntryIds = roomService.getEntriesByRoomId(roomId)
                .stream()
                .map(RoomEntry::getId)
                .collect(Collectors.toList());

        // 참가자 목록으로 게임을 생성한다.
        Game newGame = gameService.createGame(roomId, roomEntryIds);

        // 게임 시작 이벤트를 브로드캐스팅한다.
        ServerEvent event = ServerEvent.builder()
                .type(ServerEventType.GAME_START_BROADCAST)
                .cid(cid)
                .clientAt(sentAt)
                .payload(newGame)
                .build();

        publisher.publish(Dest.roomEvent(roomId), event);

        return Ack.ok(cid, newGame.getId());
    }
}