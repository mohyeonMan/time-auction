package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

import java.util.List;

public interface RoomService {

    Room createRoom(String roomName, String userId);

    void deleteRoom(String roomId);

    List<RoomEntry> getEntriesByRoomId(String roomId);

    List<RoomEntry> getReadyUsers(String roomId);

    RoomEntry joinRoom(String roomId, String userId);

    RoomEntry leaveRoom(String roomId, String userId);

    RoomEntry setReady(String roomId, String userId, boolean isReady);

    void setParticipation(String roomId, String userId, boolean isParticipating);

}
