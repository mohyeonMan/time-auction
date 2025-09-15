package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.game.model.Game;

import java.util.List;

public interface GameService {
    Game createGame(String roomId, List<String> roomEntryIds);

    Game getGame(String gameId);

    void startGame(String gameId);

    void startNextRound(String gameId);

    void endGame(String gameId);
}