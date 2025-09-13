package com.jhpark.time_auction.room.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.jhpark.time_auction.common.ws.model.Ack;
import com.jhpark.time_auction.common.ws.model.Dest;
import com.jhpark.time_auction.common.ws.model.Meta;
import com.jhpark.time_auction.common.ws.model.ServerEvent;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    
    @MessageMapping("/room/create")
    @SendToUser(Dest.USER_ACK)
    public Ack<Object> createRoom(
        Principal principal, 
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt,
        @Payload String roomName
    ) {

        ServerEvent event = roomService.createRoom(
            cid,
            sentAt,
            principal.getName(),
            roomName
        );

        return Ack.ok(event.getMeta(), event.getPayload());
    }

    @MessageMapping("/room/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<ServerEvent> getRooms(
        Principal principal,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ) {
        ServerEvent event = roomService.getRooms();
        return Ack.ok(event.getMeta(), event.getPayload());
    }

    @MessageMapping("/room/{roomId}/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<Room> getRoomByRoomId(
        Principal principal, 
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ) {
        ServerEvent event = roomService.getRoomByRoomId(roomId);
        return Ack.ok(event.getMeta(), event.getPayload());
    }

    @MessageMapping("/room-entries/{roomId}/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<List<RoomEntry>> getRoomEntriesByRoomId(
        Principal principal, 
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ) {
        List<RoomEntry> entries = roomService.getEntriesByRoomId(roomId);
        return Ack.ok(cid, entries);
    }

    @MessageMapping("/room-entries/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<List<RoomEntry>> getRoomEntriesByRoomId(
        Principal principal,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ) {
        List<RoomEntry> entries = roomService.getEntries();
        return Ack.ok(cid, entries);
    }

    @MessageMapping("/room/{roomId}/join")
    @SendTo(Dest.USER_ACK)
    public Ack<Object> joinRoom(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt,
        @Payload String nickname
    ){
        ServerEvent joinRoomEvent = roomService.joinRoom(
            cid,
            sentAt,
            roomId,
            principal.getName(),
            nickname
        );
        return Ack.ok(cid, joinRoomEvent.getSid(), joinRoomEvent.getPayload());
    }

    @MessageMapping("/room/{roomId}/ready")
    @SendTo(Dest.USER_ACK)
    public Ack<RoomEntry> ready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ){
        RoomEntry roomEntry = roomService.ready(roomId, principal.getName());
        return Ack.ok(cid, roomEntry);
    }
    
    @MessageMapping("/room/{roomId}/unready")
    @SendTo(Dest.USER_ACK)
    public Ack<RoomEntry> unready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ){
        RoomEntry roomEntry = roomService.ready(roomId, principal.getName());
        return Ack.ok(cid, roomEntry);
    }

}
