package com.jhpark.time_auction.room.repository;

import com.jhpark.time_auction.room.model.RoomEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomEntryRepository extends CrudRepository<RoomEntry, String> {
    List<RoomEntry> findAll();
    List<RoomEntry> findAllByRoomId(String roomId);
    Optional<RoomEntry> findByRoomIdAndSessionId(String roomId, String sessionId);
    Optional<RoomEntry> findByIdAndRoomId(String id, String roomId);
    void deleteByRoomId(String roomId);
    Optional<RoomEntry> findBySessionId(String sessionId);
}