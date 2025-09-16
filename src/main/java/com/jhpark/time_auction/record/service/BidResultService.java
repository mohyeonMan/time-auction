package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidResult;

import java.util.List;

public interface BidResultService {
    
    BidResult recordBidResult(String roundId, String gameEntryId, long startTime, long endTime, long remainingTimeAfterRound);

    List<BidResult> findByRoundId(String roundId);

    boolean clearBidResultByRoundId(String roundId);
    boolean clearBidResultByRoundParticipantId(String roundParticipantId);
}