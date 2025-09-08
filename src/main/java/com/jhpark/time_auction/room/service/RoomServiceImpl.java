package com.jhpark.time_auction.room.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.user.model.User;

@Service
public class RoomServiceImpl implements RoomService{

    @Override
    public void createRoom(Room room) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRoom'");
    }

    @Override
    public void deleteRoom(Room room) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoom'");
    }

    @Override
    public List<RoomEntry> getEntriesByRoomId(String roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEntriesByRoomId'");
    }

    @Override
    public RoomEntry joinRoom(String roomId, User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'joinRoom'");
    }

    @Override
    public RoomEntry leftRoom(String roomId, User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leftRoom'");
    }

    @Override
    public void keepAlive(String roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keepAlive'");
    }
    
}
