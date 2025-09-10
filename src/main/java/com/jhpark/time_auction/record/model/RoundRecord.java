package com.jhpark.time_auction.record.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Data
@RedisHash("round_records")
public class RoundRecord {
    @Id
    private String id; // Redis key
    private String userId;
    private String roundId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
}