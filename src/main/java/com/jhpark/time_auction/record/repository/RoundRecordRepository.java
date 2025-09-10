package com.jhpark.time_auction.record.repository;

import com.jhpark.time_auction.record.model.RoundRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoundRecordRepository extends CrudRepository<RoundRecord, String> {
    Optional<RoundRecord> findByUserIdAndRoundId(String userId, String roundId);
    List<RoundRecord> findAllByRoundId(String roundId);
}
