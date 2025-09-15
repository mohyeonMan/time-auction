package com.jhpark.time_auction.record.controller;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.record.handler.RecordEventHandler;
import com.jhpark.time_auction.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordEventHandler recordEventHandler;
    private final RoomService roomService; // roomEntryId 조회를 위해 주입

    @MessageMapping("/round/{roundId}/time/start")
    @SendToUser
    public Ack<?> recordTimeStart(
            Principal principal,
            @DestinationVariable("roundId") String roundId,
            @Header(name="x-msg-id") String cid,
            @Header(name="x-sent-at") long sentAt
    ) {
        String roomEntryId = roomService.getRoomEntryBySessionId(principal.getName()).getId();
        return recordEventHandler.handleTimeStart(cid, sentAt, roundId, roomEntryId, sentAt);
    }

    @MessageMapping("/round/{roundId}/time/end")
    @SendToUser
    public Ack<?> recordTimeEnd(
            Principal principal,
            @DestinationVariable("roundId") String roundId,
            @Header(name="x-msg-id") String cid,
            @Header(name="x-sent-at") long sentAt
    ) {
        String roomEntryId = roomService.getRoomEntryBySessionId(principal.getName()).getId();
        return recordEventHandler.handleTimeEnd(cid, sentAt, roundId, roomEntryId, sentAt);
    }
}
