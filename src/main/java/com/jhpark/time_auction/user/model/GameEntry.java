package com.jhpark.time_auction.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("game_entries")
public class GameEntry {
    @Id
    private String id; // ge_UUID.substring(0,8) 형태의 고유 ID
    @Indexed
    private String gameId;
    @Indexed
    private String roomEntryId; // RoomEntry의 ID
    private long remainingTime; // 플레이어에게 남은 총 시간
    private int roundsWon;
    @TimeToLive
    private Long ttl; // 게임이 끝날 때까지 유지

    public static GameEntry create(String gameId, String roomEntryId, long initialTime) {
        String id = "ge_" + UUID.randomUUID().toString().substring(0, 8);
        return new GameEntry(id, gameId, roomEntryId, initialTime, 0, 3600L); // TTL 1시간
    }
}