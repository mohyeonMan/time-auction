package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.repository.RoomEntryRepository;
import com.jhpark.time_auction.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomEntryRepository roomEntryRepository;
    private final SimpMessageSendingOperations publisher;

    private final static long ROOM_TTL = 600;
    private final static long ROOM_ENTRY_TTL = 600;


    @Override
    public Room createRoom(
        String sessionId,
        String roomName
    ) {
        Room newRoom = Room.create(roomName, sessionId, ROOM_TTL);
        log.info("Room created: {}", newRoom);
        return roomRepository.save(newRoom);

    }

    @Override
    public Room getRoomByRoomId(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomMessageException("Room not found : " + roomId));
    }

    // @Override
    // public List<Room> getRooms() {
    //      return roomRepository.findAll();
    // }

    @Override
    public void  deleteRoom(String roomId) {
        roomEntryRepository.deleteByRoomId(roomId);
        roomRepository.deleteById(roomId);
        log.info("Room deleted: {}", roomId);

    }

    @Override
    public List<RoomEntry> getEntriesByRoomId(String roomId) {
        return roomEntryRepository.findAllByRoomId(roomId);
    }

    // @Override
    // public List<RoomEntry> getEntries() {
    //     return roomEntryRepository.findAll();
    // }

    @Override
    public List<RoomEntry> getReadyEntries(String roomId) {
        return roomEntryRepository.findAllByRoomId(roomId).stream()
                .filter(RoomEntry::isReady)
                .collect(Collectors.toList());
    }

    @Override
    public RoomEntry joinRoom(String roomId, String sessionId, String nickname) {
        roomRepository.findById(roomId).orElseThrow(() -> new CustomMessageException("Room not found: " + roomId));
        roomEntryRepository.findByRoomIdAndSessionId(roomId, sessionId).ifPresent(entry -> {
            throw new CustomMessageException("User already in room: " + nickname);
        });

        return roomEntryRepository.save(RoomEntry.create(roomId, sessionId, nickname, ROOM_ENTRY_TTL));

    }

    @Override
    public RoomEntry leaveRoom(String roomId, String roomEntryId) {
        RoomEntry entry = roomEntryRepository.findByIdAndRoomId(roomEntryId, roomId)
                .orElseThrow(() -> new CustomMessageException("Room entry not found: " + roomEntryId));
        roomEntryRepository.delete(entry);
        log.info("User {} left room {}", entry.getNickname(), roomId);

        return entry;

    }

    @Override
    public RoomEntry ready(String roomId, String roomEntryId) {
        RoomEntry entry = roomEntryRepository.findByIdAndRoomId(roomEntryId, roomId)
                .orElseThrow(() -> new CustomMessageException("Room entry not found: " + roomEntryId));

        entry.setReady(true);
        RoomEntry savedEntry = roomEntryRepository.save(entry);

        log.info("User {} is ready", entry.getNickname());

        return savedEntry;
    }

    @Override
    public RoomEntry unready(String roomId, String roomEntryId) {
        RoomEntry entry = roomEntryRepository.findByIdAndRoomId(roomEntryId, roomId)
                .orElseThrow(() -> new CustomMessageException("User not in room"));
        entry.setReady(false);
        RoomEntry savedEntry = roomEntryRepository.save(entry);

        return savedEntry;
    }

    @Override
    public RoomEntry getRoomEntryBySessionId(String sessionId) {
        return roomEntryRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomMessageException("RoomEntry not found for session: " + sessionId));
    }
}
