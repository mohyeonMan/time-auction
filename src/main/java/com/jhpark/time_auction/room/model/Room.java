package com.jhpark.time_auction.room.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("rooms")
public class Room {
    @Id
    private String roomId;
    private String roomName;
    private String masterId;
    private long createdAt;

    public static Room create(String roomName, String creatorId) {
        return new Room(
            "room_"+UUID.randomUUID().toString().substring(0, 8),
            roomName,
            creatorId,
            Instant.now().toEpochMilli()
        );
    }
}
