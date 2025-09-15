package com.jhpark.time_auction.user.service;

import com.jhpark.time_auction.user.model.GameEntry;

public interface GameEntryService {
    GameEntry createGameEntry(String gameId, String roomEntryId, long initialTime);
    GameEntry getGameEntry(String gameId, String roomEntryId);
    GameEntry updateRemainingTime(String gameId, String roomEntryId, long consumedTime);
    GameEntry updateRoundsWon(String gameId, String roomEntryId, int roundsWon);
}
