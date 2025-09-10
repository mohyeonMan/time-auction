package com.jhpark.time_auction.common.ws.handler;

import java.time.LocalDateTime;

public interface RoomManager {

    boolean isAllReadyToPlay(String roomId);
    
    void ready(String roomEntryId);

    void notReady(String roomEntryId);

    void chat(String roomId, String sessionKey, String message, LocalDateTime sentAt);

}
