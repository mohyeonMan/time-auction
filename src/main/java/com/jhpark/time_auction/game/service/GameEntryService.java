package com.jhpark.time_auction.game.service;

import java.util.List;

import com.jhpark.time_auction.game.model.GameEntry;

public interface GameEntryService {

    GameEntry createGameEntry(String gameId, String roomEntryId, long initialTime);
    
    List<GameEntry> getGameEntries(String gameId);
    
    GameEntry getGameEntry(String gameEntryId);
    GameEntry updateRemainingTime(String gameEntryId, long consumedTime);
    GameEntry increaseRoundsWon(String gameEntryId);
}
