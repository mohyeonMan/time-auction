package com.jhpark.time_auction.game.controller;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.game.handler.RoundEventHandler;
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
public class RoundController {

    private final RoundEventHandler roundEventHandler;
    private final RoomService roomService; // roomEntryId 조회를 위해 주입

    @MessageMapping("/round/{roundId}/round-in")
    @SendToUser
    public Ack<?> optInToRound(
            Principal principal,
            @DestinationVariable("roundId") String roundId,
            @Header(name="x-msg-id") String cid,
            @Header(name="x-sent-at") long sentAt
    ) {
        String roomEntryId = roomService.getRoomEntryBySessionId(principal.getName()).getId();
        return roundEventHandler.handleOptIn(cid, sentAt, roundId, roomEntryId);
    }

    @MessageMapping("/round/{roundId}/round-out")
    @SendToUser
    public Ack<?> optOutOfRound(
            Principal principal,
            @DestinationVariable("roundId") String roundId,
            @Header(name="x-msg-id") String cid,
            @Header(name="x-sent-at") long sentAt
    ) {
        String roomEntryId = roomService.getRoomEntryBySessionId(principal.getName()).getId();
        return roundEventHandler.handleOptOut(cid, sentAt, roundId, roomEntryId);
    }
}
