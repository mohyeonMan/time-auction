package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.game.model.Round;

public interface RoundService {
    Round createRound(String gameId, int roundNumber); // gameId와 모든 참가자 ID를 받음

    void optInToRound(String roundId, String roomEntryId);
    void optOutOfRound(String roundId, String roomEntryId);

    void startBidding(String roundId);

    void settleRound(String roundId); // settleRound도 gameId를 받음

    // BidLog 기록 및 BidResult 생성 로직
    void recordBidLogStart(String roundId, String roomEntryId, long timestamp);
    void recordBidLogEnd(String roundId, String roomEntryId, long timestamp); // recordBidLogEnd도 gameId를 받음
}
