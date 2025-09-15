package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidLog;
import com.jhpark.time_auction.record.model.BidLogType;

import java.util.Optional;

public interface BidLogService {
    BidLog recordBidLogStart(String roundId, String roomEntryId, long timestamp);
    BidLog recordBidLogEnd(String roundId, String roomEntryId, long timestamp);
    Optional<BidLog> findByRoundIdAndRoomEntryIdAndType(String roundId, String roomEntryId, BidLogType type);
    void deleteBidLog(String bidLogId);
}
