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
public class Room {
    private String roomId;
    private String roomName;
    private String masterId;
    private long createdAt;

    public static Room create(String roomName, String creatorId) {
        return new Room(
            UUID.randomUUID().toString(),
            roomName,
            creatorId,
            Instant.now().toEpochMilli()
        );
    }
}
