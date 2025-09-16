package com.jhpark.time_auction.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("round_participations")
public class RoundParticipation {
    @Id
    private String id; // {roundId}_{roomEntryId}
    @Indexed
    private String roundId;
    @Indexed
    private String gameEntryId;
    // private boolean hasResponded = false; // 참여/불참 응답 여부
    // private boolean isParticipating = false; // 참여 선택 여부 (true: 참여, false: 불참)
    @TimeToLive
    private Long ttl = 3600L; // 1시간

    public static RoundParticipation create(String roundId, String gameEntryId) {
        String id = "rp_"+UUID.randomUUID().toString().substring(0, 8);
        return new RoundParticipation(id, roundId, gameEntryId,  3600L);
    }
}
