package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.RoundRecord;
import com.jhpark.time_auction.record.model.TimeWallet;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordService {
    RoundRecord startRecord(String userId, String roundId, LocalDateTime time);

    RoundRecord endRecord(String userId, String roundId, LocalDateTime time);

    void createTimeWallets(String gameId, List<String> userIds);

    TimeWallet getTimeWallet(String userId, String gameId);

    List<RoundRecord> getRoundResult(String roundId);

    void addWinToUser(String userId, String gameId);
}

