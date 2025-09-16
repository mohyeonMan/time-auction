package com.jhpark.time_auction.record.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("bid_results")
public class BidResult {
    @Id
    private String id; // {roundId}_{roomEntryId}
    @Indexed
    private String roundId;
    @Indexed
    private String roundParticipantId;
    
    private long startTime;
    private long endTime;
    private long consumedTime;
    
    private boolean isWinner = false;
    private long remainingTimeAfterRound;

    @TimeToLive
    private Long ttl = 3600L; // 1시간 (게임이 끝날 때까지 유지)

    public static BidResult create(String roundId, String roomEntryId, long startTime, long endTime, long consumedTime, long remainingTimeAfterRound) {
        String id = roundId + "_" + roomEntryId;
        return new BidResult(id, roundId, roomEntryId, startTime, endTime, consumedTime, false, remainingTimeAfterRound, 3600L);
    }
}
