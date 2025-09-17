package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidResult;

import java.util.List;
import java.util.Set;

public interface BidResultService {
    
    BidResult recordBidResult(String roundId, String gameEntryId, long consumed, long remainingTimeAfterRound);

    Set<String> getFinishedGameEntryIds(String roundId);
    BidResult getMostConsumedBidResult(String roundId);

    boolean clearBidResultByRoundId(String roundId);
    boolean clearBidResultByRoundParticipantId(String roundParticipantId);
}