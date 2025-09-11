package com.jhpark.time_auction.room.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

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
    @SendToUser("/queue/messages")
    public Room createRoom(
        Principal principal, 
        CreateRoomEvent request
    ) {
        Room room = roomService.createRoom(request.getRoomName(), principal.getName());

        return room;
    }

    @MessageMapping("/room/get")
    @SendToUser("/queue/messages")
    public List<Room> getRooms(
        Principal principal
    ) {
        List<Room> rooms = roomService.getRooms();
        return rooms;
    }

    @MessageMapping("/room/get/{roomId}")
    @SendToUser("/queue/messages")
    public Room getRoomByRoomId(
        Principal principal, 
        @DestinationVariable("roomId") String roomId
    ) {
        Room room = roomService.getRoomByRoomId(roomId);
        return room;
    }

    @MessageMapping("/room-entries/get/{roomId}")
    @SendToUser("/queue/messages")
    public List<RoomEntry> getRoomEntriesByRoomId(
        Principal principal, 
        @DestinationVariable("roomId") String roomId
    ) {
        List<RoomEntry> room = roomService.getEntriesByRoomId(roomId);
        return room;
    }

    @MessageMapping("/room-entries/get")
    @SendToUser("/queue/messages")
    public List<RoomEntry> getRoomEntriesByRoomId(
        Principal principal
    ) {
        List<RoomEntry> room = roomService.getEntries();
        return room;
    }

    @MessageMapping("/room/join/{roomId}")
    @SendToUser("/queue/messages")
    public RoomEntry joinRoom(
        Principal principal,
        @DestinationVariable("roomId") String roomId
    ){
        return roomService.joinRoom(roomId, principal.getName());
    }

}
