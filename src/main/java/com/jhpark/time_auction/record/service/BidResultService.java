package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidResult;

import java.util.List;

public interface BidResultService {
    BidResult createBidResult(String roundId, String roomEntryId, long startTime, long endTime, long consumedTime, long remainingTimeAfterRound);
    List<BidResult> findByRoundId(String roundId);
    BidResult save(BidResult bidResult);
}