package com.jhpark.time_auction.room.handler;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.service.RoomEntryService;
import com.jhpark.time_auction.room.service.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomEventHandler {
    private final RoomService roomService;
    private final RoomEntryService roomEntryService;
    private final MessagePublisher<ServerEvent> publisher;

    // public Ack<?> handleCreateEvent(
    //     String cid,
    //     long sentAt,
    //     String sessionId,
    //     String roomName
    // ){

    //     Room room = roomService.createRoom(sessionId, roomName);
        
    //     ServerEvent event = ServerEvent.builder()
    //         .type(ServerEventType.CREATE_ROOM)
    //         .cid(sessionId)
    //         .clientAt(sentAt)
    //         .payload(room)
    //         .build();
        
    //     return Ack.ok(cid, room.getRoomId());
    // }

    // public Ack<?> handleJoinEvent(
    //     String cid,
    //     long sentAt,
    //     String roomId,
    //     String sessionId,
    //     String nickname
    // ){

    //     RoomEntry entry = roomService.joinRoom(roomId, sessionId, nickname);

    //     ServerEvent event = ServerEvent.builder()
    //         .type(ServerEventType.JOIN_CONFIRM)
    //         .cid(cid)
    //         .clientAt(sentAt)
    //         .payload(entry)
    //         .build();

    //     publisher.publish(Dest.roomEvent(entry.getRoomId()), event);

    //     return Ack.ok(cid, entry.getId());
    // }

    // public Ack<?> handleLeftEvent(
    //     String cid,
    //     long sentAt,
    //     String roomId,
    //     String roomEntryId
    // ){
    //     RoomEntry entry = roomService.leaveRoom(roomId, roomEntryId);

    //     ServerEvent event = ServerEvent.builder()
    //         .type(ServerEventType.LEAVE_CONFIRM)
    //         .cid(cid)
    //         .clientAt(sentAt)
    //         .payload(entry)
    //         .build();

    //     publisher.publish(Dest.roomEvent(entry.getRoomId()), event);

    //     return Ack.ok(cid, entry.getId());
    // }

    public Ack<?> handleReadyEvent(
        String cid,
        long sentAt,
        String roomId,
        String roomEntryId
    ){
        RoomEntry entry = roomEntryService.ready(roomId, roomEntryId);

        ServerEvent event = ServerEvent.builder()
            .type(ServerEventType.READY_CONFIRM)
            .cid(cid)
            .clientAt(sentAt)
            .payload(entry)
            .build();

        publisher.publish(Dest.roomEvent(entry.getRoomId()), event);

        return Ack.ok(cid, entry.getId());

    }

    public Ack<?> handleUnreadyEvent(
        String cid,
        long sentAt,
        String roomId,
        String roomEntryId
    ){
        RoomEntry entry = roomEntryService.unready(roomId, roomEntryId); 
        ServerEvent event = ServerEvent.builder()
            .type(ServerEventType.NOT_READY_CONFIRM)
            .cid(cid)
            .clientAt(sentAt)
            .payload(entry)
            .build();

        publisher.publish(Dest.roomEvent(entry.getRoomId()), event);

        return Ack.ok(cid, entry.getId());
    }
    

}
