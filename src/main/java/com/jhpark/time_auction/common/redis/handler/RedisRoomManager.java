package com.jhpark.time_auction.common.redis.handler;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.common.ws.handler.RoomManager;
import com.jhpark.time_auction.room.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRoomManager implements RoomManager {

    private final RoomService roomService;

    @Override
    public boolean isAllReadyToPlay(String roomId) {
        return roomService.isAllReadyToPlay(roomId);
    }

    @Override
    public void ready(String roomEntryId) {
        roomService.ready(roomEntryId);
    }

    @Override
    public void notReady(String roomEntryId) {
        roomService.unready(roomEntryId);
    }

    @Override
    public void chat(String roomId, String sessionKey, String message, LocalDateTime sentAt) {
        throw new UnsupportedOperationException("Unimplemented method 'chat'");
    }

    
    

}
