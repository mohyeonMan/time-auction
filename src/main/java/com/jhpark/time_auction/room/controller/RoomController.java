package com.jhpark.time_auction.room.controller;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.room.handler.RoomEventHandler;
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
public class RoomController {

    private final RoomEventHandler roomEventHandler;
    private final RoomService roomService; // roomEntryId 조회를 위해 주입
    
    @MessageMapping("/room/create")
    @SendToUser
    public Ack<?> createRoom(
        Principal principal, 
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt,
        @Payload String roomName
    ) {
        String sessionId = principal.getName();
        return roomEventHandler.handleCreateEvent(cid, sentAt, sessionId, roomName);
    }

    @MessageMapping("/room/{roomId}/join")
    @SendToUser // 요청자에게 Ack 응답
    public Ack<?> joinRoom(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt,
        @Payload String nickname
    ){
        String sessionId = principal.getName();
        return roomEventHandler.handleJoinEvent(cid, sentAt, roomId, sessionId, nickname);
    }

    @MessageMapping("/room/{roomId}/room-entry/{roomEntryId}/ready")
    @SendToUser // 요청자에게 Ack 응답
    public Ack<?> ready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @DestinationVariable("roomEntryId") String roomEntryId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ){
        // roomEntryId가 principal의 sessionId와 일치하는지 검증 로직 필요
        String sessionId = principal.getName();
        if (!roomEntryId.equals(roomService.getRoomEntryBySessionId(sessionId).getId())) {
            throw new IllegalArgumentException("Invalid roomEntryId for session");
        }
        return roomEventHandler.handleReadyEvent(cid, sentAt, roomId, roomEntryId);
    }
    
    @MessageMapping("/room/{roomId}/room-entry/{roomEntryId}/unready")
    @SendToUser // 요청자에게 Ack 응답
    public Ack<?> unready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @DestinationVariable("roomEntryId") String roomEntryId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ){
        // roomEntryId가 principal의 sessionId와 일치하는지 검증 로직 필요
        String sessionId = principal.getName();
        if (!roomEntryId.equals(roomService.getRoomEntryBySessionId(sessionId).getId())) {
            throw new IllegalArgumentException("Invalid roomEntryId for session");
        }
        return roomEventHandler.handleUnreadyEvent(cid, sentAt, roomId, roomEntryId);
    }

}