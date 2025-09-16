package com.jhpark.time_auction.game.service;

import java.util.List;

import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.model.RoundParticipation;

public interface RoundService {

    /*
     * before : readyNextRound
     * 해당 gameEntryId의 라운드 참여여부 확인.
     * 모든 gameEntry가 선택해야 다음으로 넘어감.
     * next : 
     */
    RoundParticipation roundIn(String roundId, String gameEntryId);         //라운드 참여
    RoundParticipation roundOut(String roundId, String gameEntryId);        //라운드 미참여

    /*
     * before : roundIn, roundOut
     * round가 시작됨.
     */
    // Round startBidding(String roundId);
    // Round startRound(String gameId, int roundNumber);                       //라운드 시작
    
    boolean checkAllResponded(String roundId, List<String> gameEntries);
    
    // BidLog recordBidLogStart(String roundId, String gameEntryId, long timestamp);
    // BidLog recordBidLogEnd(String roundId, String gameEntryId, long timestamp);

    Round settleRound(String roundId);                                      //라운드 종료.

    boolean clearRound(String roundId);
}
