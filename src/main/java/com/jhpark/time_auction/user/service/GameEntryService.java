package com.jhpark.time_auction.user.service;

import java.util.List;

import com.jhpark.time_auction.user.model.GameEntry;

public interface GameEntryService {

    GameEntry createGameEntry(String gameId, String roomEntryId, long initialTime);
    
    List<GameEntry> getGameEntries(String gameId);
    GameEntry getGameEntry(String gameId, String roomEntryId);
    GameEntry updateRemainingTime(String gameId, String roomEntryId, long consumedTime);
    GameEntry updateRoundsWon(String gameId, String roomEntryId, int roundsWon);
}
