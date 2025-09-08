package com.jhpark.time_auction.room.repository;

import java.util.List;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

public interface RoomRepository {

    Room saveRoom(String roomName, String sessionId);

    Room deleteRoomByRoomId(String roomId);

    Room updateMasterId(String roomId);

    Room expireRoom();

    RoomEntry saveRoomEntry(String roomId, String sessionId);

    RoomEntry deleteRoomEntryByRoomEntryId(String roomEntryId);

    List<RoomEntry> getRoomEntriesByRoomId(String roomId);


}
