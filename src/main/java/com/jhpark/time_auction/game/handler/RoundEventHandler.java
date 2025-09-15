package com.jhpark.time_auction.game.handler;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.game.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundEventHandler {
    private final RoundService roundService;
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleOptIn(String cid, long sentAt, String roundId, String roomEntryId) {
        roundService.optInToRound(roundId, roomEntryId);

        // 라운드 참가 이벤트 브로드캐스팅
        ServerEvent event = ServerEvent.builder()
                .type(ServerEventType.ROUND_IN_CONFIRM)
                .cid(cid)
                .clientAt(sentAt)
                .payload(String.format("User %s has opted in to round %s", roomEntryId, roundId))
                .build();

        // TODO: gameId를 통해 roomId를 조회하여 publish 해야 함
        // publisher.publish(Dest.roomEvent(roomId), event);

        return Ack.ok(cid, null);
    }

    public Ack<?> handleOptOut(String cid, long sentAt, String roundId, String roomEntryId) {
        roundService.optOutOfRound(roundId, roomEntryId);

        // 라운드 불참 이벤트 브로드캐스팅
        ServerEvent event = ServerEvent.builder()
                .type(ServerEventType.ROUND_OUT_CONFIRM)
                .cid(cid)
                .clientAt(sentAt)
                .payload(String.format("User %s has opted out of round %s", roomEntryId, roundId))
                .build();

        // TODO: gameId를 통해 roomId를 조회하여 publish 해야 함
        // publisher.publish(Dest.roomEvent(roomId), event);

        return Ack.ok(cid, null);
    }
}
