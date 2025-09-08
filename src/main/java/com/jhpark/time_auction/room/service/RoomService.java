package com.jhpark.time_auction.room.service;

import java.util.List;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.user.model.User;

public interface RoomService {

    void createRoom(Room room);

    void deleteRoom(Room room);

    List<RoomEntry> getEntriesByRoomId(String roomId);

    RoomEntry joinRoom(String roomId, User user);

    RoomEntry leftRoom(String roomId, User user);

    void keepAlive(String roomId);

}
