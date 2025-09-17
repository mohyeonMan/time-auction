package com.jhpark.time_auction.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("games")
public class Game {
    @Id
    private String id;

    @Indexed
    private String roomId;

    private int gameEntryCounts;

    private int totalRounds = 5; // 기본 5 라운드

    private Long totalTime = 300L; // 플레이어에게 주어지는 총 시간 (기본 5분)

    @Setter
    private int currentRound = 0;
    
    @Setter
    private GameStatus status;

    @TimeToLive
    private Long ttl = 3600L; // 1시간

    public static Game create(String roomId) {
        String gameId = "game_" + UUID.randomUUID().toString().substring(0, 8);
        // 기본값: 5라운드, 총 시간 300초
        return new Game(gameId, roomId, 0, 5, 300L, 0, GameStatus.READY, 3600L);
    }
}
