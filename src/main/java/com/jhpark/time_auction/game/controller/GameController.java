package com.jhpark.time_auction.game.controller;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.game.handler.GameEventHandler;
import com.jhpark.time_auction.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameEventHandler gameEventHandler;

    @MessageMapping("/room/{roomId}/game/start")
    @SendToUser // 요청자에게 Ack 응답
    public Ack<?> startGame(
            Principal principal,
            @DestinationVariable("roomId") String roomId,
            @Header(name="x-msg-id") String cid,
            @Header(name="x-sent-at") long sentAt,
            @Payload(required = false) Integer totalRound,
            @Payload(required = false) Integer totalTime
    ) {
        return gameEventHandler.handleStartGame(cid, sentAt, roomId, totalRound, totalTime, principal.getName());
    }

    
}
