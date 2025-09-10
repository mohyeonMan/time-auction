package com.jhpark.time_auction.record.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("time_wallets")
public class TimeWallet {
    @Id
    private String id; // Redis key
    private String userId;
    private String gameId;
    private long timeLeft;
    private int roundWins = 0;
}