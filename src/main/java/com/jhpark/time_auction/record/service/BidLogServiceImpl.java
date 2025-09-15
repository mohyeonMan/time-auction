package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidLog;
import com.jhpark.time_auction.record.model.BidLogType;
import com.jhpark.time_auction.record.repository.BidLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidLogServiceImpl implements BidLogService {

    private final BidLogRepository bidLogRepository;

    @Override
    public BidLog recordBidLogStart(String roundId, String roomEntryId, long timestamp) {
        BidLog newLog = BidLog.create(roundId, roomEntryId, timestamp, BidLogType.START);
        return bidLogRepository.save(newLog);
    }

    @Override
    public BidLog recordBidLogEnd(String roundId, String roomEntryId, long timestamp) {
        BidLog newLog = BidLog.create(roundId, roomEntryId, timestamp, BidLogType.END);
        return bidLogRepository.save(newLog);
    }

    @Override
    public Optional<BidLog> findByRoundIdAndRoomEntryIdAndType(String roundId, String roomEntryId, BidLogType type) {
        return bidLogRepository.findByRoundIdAndRoomEntryIdAndType(roundId, roomEntryId, type);
    }

    @Override
    public void deleteBidLog(String bidLogId) {
        bidLogRepository.deleteById(bidLogId);
    }
}
