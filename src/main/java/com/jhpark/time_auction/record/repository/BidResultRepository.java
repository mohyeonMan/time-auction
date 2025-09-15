package com.jhpark.time_auction.record.repository;

import com.jhpark.time_auction.record.model.BidResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BidResultRepository extends CrudRepository<BidResult, String> {
    List<BidResult> findByRoundId(String roundId);
    Optional<BidResult> findByRoundIdAndRoomEntryId(String roundId, String roomEntryId);
}