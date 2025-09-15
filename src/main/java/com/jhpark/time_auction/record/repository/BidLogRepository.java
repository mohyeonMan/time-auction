package com.jhpark.time_auction.record.repository;

import com.jhpark.time_auction.record.model.BidLog;
import com.jhpark.time_auction.record.model.BidLogType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BidLogRepository extends CrudRepository<BidLog, String> {
    Optional<BidLog> findByRoundIdAndRoomEntryIdAndType(String roundId, String roomEntryId, BidLogType type);
}
