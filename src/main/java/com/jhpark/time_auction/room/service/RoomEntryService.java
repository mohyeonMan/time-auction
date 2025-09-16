package com.jhpark.time_auction.room.service;

import com.jhpark.time_auction.room.model.RoomEntry;

import java.util.List;

public interface RoomEntryService {

    List<RoomEntry> getEntriesByRoomId(String roomId);                      //방 참가자 조회
    // List<RoomEntry> getEntries();
    List<RoomEntry> getReadyEntries(String roomId);                         //준비한 참가자 조회
    RoomEntry ready(String roomId, String roomEntryId);                     //준비
    RoomEntry unready(String roomId, String roomEntryId);                   //준비 취소

    RoomEntry getRoomEntryBySessionId(String sessionId);                    //sessionId로 참가자 조회.
}