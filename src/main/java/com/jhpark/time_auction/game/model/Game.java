package com.jhpark.time_auction.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("games")
public class Game {
    @Id
    private String id;
    private String roomId;
    private int currentRound = 0;
}
