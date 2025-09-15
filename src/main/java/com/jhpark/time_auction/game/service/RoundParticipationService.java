package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.game.model.RoundParticipation;

import java.util.List;

public interface RoundParticipationService {
    void createAllParticipationsForRound(String roundId, List<String> roomEntryIds);
    RoundParticipation updateParticipation(String roundId, String roomEntryId, boolean isParticipating);
    boolean checkAllResponded(String roundId, int totalParticipants);
    List<String> getActualParticipants(String roundId);
}
