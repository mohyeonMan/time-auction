package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidResult;
import com.jhpark.time_auction.record.repository.BidResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidResultServiceImpl implements BidResultService {

    private final BidResultRepository bidResultRepository;

    @Override
    public BidResult createBidResult(String roundId, String roomEntryId, long startTime, long endTime, long consumedTime, long remainingTimeAfterRound) {
        BidResult newBidResult = BidResult.create(roundId, roomEntryId, startTime, endTime, consumedTime, remainingTimeAfterRound);
        return bidResultRepository.save(newBidResult);
    }

    @Override
    public List<BidResult> findByRoundId(String roundId) {
        return bidResultRepository.findByRoundId(roundId);
    }

    @Override
    public BidResult save(BidResult bidResult) {
        return bidResultRepository.save(bidResult);
    }
}