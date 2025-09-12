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

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.model.in.ClientEvent.CreateRoomEvent;
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
    public Ack<Room> createRoom(
        Principal principal, 
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt,
        @Payload CreateRoomEvent request
    ) {
        Room room = roomService.createRoom(
            request.getRoomName(), principal.getName()
        );

        return Ack.ok(cid, room);
    }

    @MessageMapping("/room/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<List<Room>> getRooms(
        Principal principal,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ) {
        List<Room> rooms = roomService.getRooms();
        return Ack.ok(cid, rooms);
    }

    @MessageMapping("/room/{roomId}/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<Room> getRoomByRoomId(
        Principal principal, 
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ) {
        Room room = roomService.getRoomByRoomId(roomId);
        return Ack.ok(cid, room);
    }

    @MessageMapping("/room-entries/{roomId}/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<List<RoomEntry>> getRoomEntriesByRoomId(
        Principal principal, 
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ) {
        List<RoomEntry> entries = roomService.getEntriesByRoomId(roomId);
        return Ack.ok(cid, entries);
    }

    @MessageMapping("/room-entries/get")
    @SendToUser(Dest.USER_ACK)
    public Ack<List<RoomEntry>> getRoomEntriesByRoomId(
        Principal principal,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ) {
        List<RoomEntry> entries = roomService.getEntries();
        return Ack.ok(cid, entries);
    }

    @MessageMapping("/room/{roomId}/join")
    @SendTo(Dest.USER_ACK)
    public Ack<RoomEntry> joinRoom(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ){
        RoomEntry roomEntry = roomService.joinRoom(roomId, principal.getName());
        return Ack.ok(cid, roomEntry);
    }

    @MessageMapping("/room/{roomId}/ready")
    @SendTo(Dest.USER_ACK)
    public Ack<RoomEntry> ready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ){
        RoomEntry roomEntry = roomService.ready(roomId, principal.getName());
        return Ack.ok(cid, roomEntry);
    }
    
    @MessageMapping("/room/{roomId}/unready")
    @SendTo(Dest.USER_ACK)
    public Ack<RoomEntry> unready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id", required=false) String cid,
        @Header(name="x-sent-at", required=false) Long sentAt
    ){
        RoomEntry roomEntry = roomService.ready(roomId, principal.getName());
        return Ack.ok(cid, roomEntry);
    }

}
