package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

import java.util.List;

public interface RoomService {

    Room createRoom(String sessionId, String roomName);                     //방 생성
    Room getRoomByRoomId(String roomId);                                    //방 조회
    // List<Room> getRooms();
    void deleteRoom(String roomId);                                         //방 삭제
    
    RoomEntry joinRoom(String roomId, String sessionId, String nickname);   //방 참가

    RoomEntry getRoomEntryBySessionId(String sessionId);                    //sessionId로 참가자 조회.
}