package com.jhpark.time_auction.room.repository;

import com.jhpark.time_auction.room.model.RoomEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomEntryRepository extends CrudRepository<RoomEntry, String> {
    List<RoomEntry> findAll();
    List<RoomEntry> findAllByRoomId(String roomId);
    Optional<RoomEntry> findByRoomIdAndSessionId(String roomId, String sessionId);
    List<RoomEntry> deleteByRoomId(String roomId);
}
