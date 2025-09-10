package com.jhpark.time_auction.room.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntry {
    private String roomId;
    private String roomEntryId;
    private String sessionId;
    private long joinedAt;
    
    public static RoomEntry create(String roomId, String sessionId) {
        return new RoomEntry(
            roomId,
            UUID.randomUUID().toString(),
            sessionId,
            Instant.now().toEpochMilli()
        );
    }
}
