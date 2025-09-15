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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("rounds")
public class Round {
    @Id
    private String id;

    @Indexed
    private String gameId;

    private int roundNumber;

    @Setter
    private RoundStatus status;

    // private Set<String> participants; // RoundParticipation 모델로 분리

    private Set<String> bidResultIds; // 이번 라운드에 생성된 BidResult의 ID 목록

    @Setter
    private String winnerEntryId;

    @TimeToLive
    private Long ttl = 3600L; // 1시간

    public static Round create(String gameId, int roundNumber) {
        String roundId = "round_" + UUID.randomUUID().toString().substring(0, 8);
        // participants는 RoundParticipationService에서 관리
        return new Round(roundId, gameId, roundNumber, RoundStatus.PARTICIPATION_CHOICE, new HashSet<>(), null, 3600L);
    }
}