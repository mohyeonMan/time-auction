package com.jhpark.time_auction.record.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("bid_logs")
public class BidLog {
    @Id
    private String id; // {roundId}_{roomEntryId}_{type}
    @Indexed
    private String roundId;
    @Indexed
    private String roundParticipantId;
    private long timestamp; // 이벤트 발생 시각
    private BidLogType type; // START 또는 END
    @TimeToLive
    private Long ttl = 600L; // 임시 로그이므로 짧은 TTL

    public static BidLog create(String roundId, String roundParticipantId, long timestamp, BidLogType type) {
        String id = "bl_"+UUID.randomUUID().toString().substring(0, 8);
        return new BidLog(id, roundId, roundParticipantId, timestamp, type, 600L);
    }
}
