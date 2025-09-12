package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

import java.util.List;

public interface RoomService {

    Room createRoom(String roomName, String sessionId);

    Room getRoomByRoomId(String roomId);

    List<Room> getRooms();

    void deleteRoom(String roomId);

    List<RoomEntry> getEntries();

    List<RoomEntry> getEntriesByRoomId(String roomId);

    List<RoomEntry> getReadyUsers(String roomId);

    RoomEntry joinRoom(String roomId, String sessionId);

    RoomEntry leaveRoom(String roomId, String sessionId);

    RoomEntry ready(String roomId, String sessionId);

    RoomEntry unready(String roomId, String sessionId);

}
