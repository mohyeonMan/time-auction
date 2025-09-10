package com.jhpark.time_auction.room.service;

import java.util.List;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

public interface RoomService {

    Room createRoom(String roomName, String sessionKey);

    Room deleteRoom(String roomId);

    List<RoomEntry> getEntriesByRoomId(String roomId);

    boolean isAllReadyToPlay(String roomId);

    RoomEntry joinRoom(String roomId, String sessionKey);

    RoomEntry leftRoom(String roomId, String sessionKey);

    boolean ready(String roomEntryId);

    boolean unready(String roomEntryId);

}
