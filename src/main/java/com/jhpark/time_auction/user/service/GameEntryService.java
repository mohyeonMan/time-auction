package com.jhpark.time_auction.user.service;

import java.util.List;

import com.jhpark.time_auction.user.model.GameEntry;

public interface GameEntryService {

    GameEntry createGameEntry(String gameId, String roomEntryId, long initialTime);
    
    List<GameEntry> getGameEntries(String gameId);
    
    GameEntry getGameEntry(String gameEntryId);
    GameEntry updateRemainingTime(String gameEntryId, long consumedTime);
    GameEntry increaseRoundsWon(String gameEntryId);
}
