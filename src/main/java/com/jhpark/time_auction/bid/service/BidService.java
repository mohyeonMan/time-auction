package com.jhpark.time_auction.bid.service;

import com.jhpark.time_auction.bid.model.BidBank;

public interface BidService {

    BidBank registBid(String roundId, String sessionId, long time);

    boolean validateBid();
}
