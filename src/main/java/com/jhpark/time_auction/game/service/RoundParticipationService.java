package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.game.model.RoundParticipation;

import java.util.Set;

public interface RoundParticipationService {

    
    RoundParticipation roundIn(String roundId, String gameEntryId);             // 라운드 참여
    RoundParticipation roundOut(String roundId, String gameEntryId);            // 라운드 미참여

    Set<String> getChoosenGameEntryIds(String roundId);                       // 전원 확인을 위한 EntrySet
    RoundParticipation getRoundParticipation(String roundParticipantId);

    boolean clearRoomParticipation();                                           // 정리
}
