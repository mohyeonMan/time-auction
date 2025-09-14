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
import com.jhpark.time_auction.room.handler.RoomEventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomEventHandler roomEventHandler;
    
    @MessageMapping("/room/create")
    @SendToUser(Dest.USER_ACK)
    public Ack<?> createRoom(
        Principal principal, 
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt,
        @Payload String roomName
    ) {
        return roomEventHandler.handleCreateEvent(cid, sentAt, principal.getName(), roomName);
    }

    // @MessageMapping("/room/get")
    // @SendToUser(Dest.USER_ACK)
    // public Ack<?> getRooms(
    //     Principal principal,
    //     @Header(name="x-msg-id") String cid,
    //     @Header(name="x-sent-at") long sentAt
    // ) {
    //     ServerEvent event = roomService.getRooms();
    //     return Ack.ok(event.getMeta(), event.getPayload());
    // }

    // @MessageMapping("/room/{roomId}/get")
    // @SendToUser(Dest.USER_ACK)
    // public Ack<Room> getRoomByRoomId(
    //     Principal principal, 
    //     @DestinationVariable("roomId") String roomId,
    //     @Header(name="x-msg-id") String cid,
    //     @Header(name="x-sent-at") long sentAt
    // ) {
    //     ServerEvent event = roomService.getRoomByRoomId(roomId);
    //     return Ack.ok(event.getMeta(), event.getPayload());
    // }

    // @MessageMapping("/room-entries/{roomId}/get")
    // @SendToUser(Dest.USER_ACK)
    // public Ack<List<RoomEntry>> getRoomEntriesByRoomId(
    //     Principal principal, 
    //     @DestinationVariable("roomId") String roomId,
    //     @Header(name="x-msg-id") String cid,
    //     @Header(name="x-sent-at") long sentAt
    // ) {
    //     List<RoomEntry> entries = roomService.getEntriesByRoomId(roomId);
    //     return Ack.ok(cid, entries);
    // }

    // @MessageMapping("/room-entries/get")
    // @SendToUser(Dest.USER_ACK)
    // public Ack<List<RoomEntry>> getRoomEntriesByRoomId(
    //     Principal principal,
    //     @Header(name="x-msg-id") String cid,
    //     @Header(name="x-sent-at") long sentAt
    // ) {
    //     List<RoomEntry> entries = roomService.getEntries();
    //     return Ack.ok(cid, entries);
    // }

    @MessageMapping("/room/{roomId}/join")
    @SendTo(Dest.USER_ACK)
    public Ack<?> joinRoom(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt,
        @Payload String nickname
    ){
        return roomEventHandler.handleJoinEvent(cid, sentAt, roomId, principal.getName(), nickname);
    }

    @MessageMapping("/room/{roomId}/room-entry/{roomEntryId}/ready")
    @SendTo(Dest.USER_ACK)
    public Ack<?> ready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @DestinationVariable("roomEntryId") String roomEntryId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ){
        return roomEventHandler.handleReadyEvent(cid, sentAt, roomId, roomEntryId);
    }
    
    @MessageMapping("/room/{roomId}/room-entry/{roomEntryId}/unready")
    @SendTo(Dest.USER_ACK)
    public Ack<?> unready(
        Principal principal,
        @DestinationVariable("roomId") String roomId,
        @DestinationVariable("roomEntryId") String roomEntryId,
        @Header(name="x-msg-id") String cid,
        @Header(name="x-sent-at") long sentAt
    ){
        return roomEventHandler.handleUnreadyEvent(cid, sentAt, roomId, roomEntryId);
    }

}
