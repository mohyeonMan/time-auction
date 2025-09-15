package com.jhpark.time_auction.record.handler;

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
public class RecordEventHandler {

    private final RoundService roundService;
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleTimeStart(String cid, long sentAt, String roundId, String roomEntryId, long timestamp) {
        roundService.recordBidLogStart(roundId, roomEntryId, timestamp);

        // 클라이언트에게 시작 확인 응답
        return Ack.ok(cid, "Time recording started.");
    }

    public Ack<?> handleTimeEnd(String cid, long sentAt, String roundId, String roomEntryId, long timestamp) {
        roundService.recordBidLogEnd(roundId, roomEntryId, timestamp);

        // 클라이언트에게 종료 확인 응답
        return Ack.ok(cid, "Time recording ended.");
    }
}