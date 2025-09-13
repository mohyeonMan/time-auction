package com.jhpark.time_auction.room.model;

import lombok.*;

import org.apache.catalina.filters.ExpiresFilter.DurationUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

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
    @Indexed
    private String roomId;
    @Indexed
    private String sessionId;
    private String nickname;
    private long joinedAt;

    private boolean isReady = false;

    @TimeToLive
    private long ttl;
    
    public static RoomEntry create(String roomId, String sessionId, String nickname, long ttl) {
        return new RoomEntry(
            UUID.randomUUID().toString(),
            roomId,
            sessionId,
            nickname,
            Instant.now().toEpochMilli(),
            false,
            ttl
        );
    }
}
