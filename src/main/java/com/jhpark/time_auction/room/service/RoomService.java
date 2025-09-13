package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

import java.util.List;

public interface RoomService {

    Room createRoom(String sessionId, String roomName);

    Room getRoomByRoomId(String roomId);

    List<Room> getRooms();

    void deleteRoom(String roomId);

    List<RoomEntry> getEntries();

    List<RoomEntry> getEntriesByRoomId(String roomId);

    List<RoomEntry> getReadyUsers(String roomId);

    RoomEntry joinRoom(String cid, long sentAt, String roomId, String sessionId, String nickname);

    RoomEntry leaveRoom(String cid, long sentAt, String roomEntryId);

    RoomEntry ready(String roomEntryId);

    RoomEntry unready(String roomEntryId);

}
