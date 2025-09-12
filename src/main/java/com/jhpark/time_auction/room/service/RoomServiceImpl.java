package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.publish.MessagePublisher;
import com.jhpark.time_auction.room.event.JoinRoomEvent;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.repository.RoomEntryRepository;
import com.jhpark.time_auction.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Server;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomEntryRepository roomEntryRepository;
    private final MessagePublisher<ServerEvent> publisher;

    private final static long ROOM_TTL = 600;
    private final static long ROOM_ENTRY_TTL = 600;


    @Override
    public Room createRoom(String roomName, String userId) {
        Room newRoom = Room.create(roomName, userId, ROOM_TTL);
        log.info("Room created: {}", newRoom);
        return roomRepository.save(newRoom);
    }

    @Override
    public Room getRoomByRoomId(String roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new CustomMessageException("Room not found : " + roomId));
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAll();

    }


    @Override
    public void deleteRoom(String roomId) {
        roomEntryRepository.deleteAll(roomEntryRepository.findAllByRoomId(roomId));
        roomRepository.deleteById(roomId);
        log.info("Room deleted: {}", roomId);
    }

    @Override
    public List<RoomEntry> getEntriesByRoomId(String roomId) {
        List<RoomEntry> entries = roomEntryRepository.findAllByRoomId(roomId);
        log.info("Room entries : {}", entries);

        return entries;
    }

    @Override
    public List<RoomEntry> getEntries() {
        List<RoomEntry> entries = roomEntryRepository.findAll();
        log.info("Room entries : {}", entries);

        return entries;
    }



    @Override
    public List<RoomEntry> getReadyUsers(String roomId) {
        return roomEntryRepository.findAllByRoomId(roomId).stream()
                .filter(RoomEntry::isReady)
                .collect(Collectors.toList());
    }

    @Override
    public RoomEntry joinRoom(String roomId, String userId) {
        roomRepository.findById(roomId).orElseThrow(() -> new CustomMessageException("Room not found: " + roomId));
        roomEntryRepository.findByRoomIdAndSessionId(roomId, userId).ifPresent(entry -> {
            throw new CustomMessageException("User already in room: " + userId);
        });
        
        RoomEntry entry = roomEntryRepository.save(RoomEntry.create(roomId, userId, ROOM_ENTRY_TTL));
        publisher.publish(publisher, new JoinRoomEvent(ServerEventType.JOIN_CONFIRM, null, roomId, userId)
        return entry;
    }

    @Override
    public RoomEntry leaveRoom(String roomId, String userId) {
        RoomEntry entry = roomEntryRepository.findByRoomIdAndSessionId(roomId, userId)
                .orElseThrow(() -> new CustomMessageException("User not in room"));
        roomEntryRepository.delete(entry);
        return entry;
    }

    @Override
    public RoomEntry setReady(String roomId, String userId, boolean isReady) {
        RoomEntry entry = roomEntryRepository.findByRoomIdAndSessionId(roomId, userId)
                .orElseThrow(() -> new CustomMessageException("User not in room"));
        entry.setReady(isReady);
        return roomEntryRepository.save(entry);
    }

    @Override
    public void setParticipation(String roomId, String userId, boolean isParticipating) {
        RoomEntry entry = roomEntryRepository.findByRoomIdAndSessionId(roomId, userId)
                .orElseThrow(() -> new CustomMessageException("User not in room"));
        entry.setParticipating(isParticipating);
        roomEntryRepository.save(entry);
    }
}