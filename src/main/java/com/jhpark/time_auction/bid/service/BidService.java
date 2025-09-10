package com.jhpark.time_auction.bid.service;

import java.time.LocalDateTime;

import com.jhpark.time_auction.bid.model.BidBank;
import com.jhpark.time_auction.bid.model.BidRecord;

public interface BidService {

    BidRecord startBid(String roundId, String sessionKey, LocalDateTime time);
    
    BidRecord endBid(String roundId, String sessionKey, LocalDateTime time);

    BidBank synchronizeBidBank(String gameId, String session);

    boolean validateBid();
}
