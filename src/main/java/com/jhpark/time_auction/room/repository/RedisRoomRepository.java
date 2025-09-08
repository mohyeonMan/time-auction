package com.jhpark.time_auction.room.repository;

import java.util.List;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

public class RedisRoomRepository implements RoomRepository{

    @Override
    public Room saveRoom(String roomName, String sessionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveRoom'");
    }

    @Override
    public Room deleteRoomByRoomId(String roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoomByRoomId'");
    }

    @Override
    public Room updateMasterId(String roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMasterId'");
    }

    @Override
    public Room expireRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'expireRoom'");
    }

    @Override
    public RoomEntry saveRoomEntry(String roomId, String sessionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveRoomEntry'");
    }

    @Override
    public RoomEntry deleteRoomEntryByRoomEntryId(String roomEntryId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoomEntryByRoomEntryId'");
    }

    @Override
    public List<RoomEntry> getRoomEntriesByRoomId(String roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoomEntriesByRoomId'");
    }
    
    
}
