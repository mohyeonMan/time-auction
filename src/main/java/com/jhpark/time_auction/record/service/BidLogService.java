package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.BidLog;

public interface BidLogService {

    BidLog recordBidLogStart(String roundParticipantId, long timestamp);                               // 배팅 시작시간 저장
    BidLog recordBidLogEnd(String roundParticipantId, long timestamp);                                 // 배팅 종료시간 저장

    BidLog getBidLogs(String roundParticipantId);

    boolean clearBidLogByRoundId(String roundId);
    boolean clearBidLogByRoundParticipantId(String roundParticipantId);
}
