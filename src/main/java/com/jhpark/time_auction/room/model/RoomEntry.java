package com.jhpark.time_auction.room.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("room_entries")
public class RoomEntry {
    @Id
    private String roomEntryId;
    private String roomId;
    private String sessionId;
    private long joinedAt;

    private boolean isReady = false;
    private boolean isParticipating = false;
    
    public static RoomEntry create(String roomId, String sessionId) {
        return new RoomEntry(
            UUID.randomUUID().toString(),
            roomId,
            sessionId,
            Instant.now().toEpochMilli(),
            false,
            false
        );
    }
}
