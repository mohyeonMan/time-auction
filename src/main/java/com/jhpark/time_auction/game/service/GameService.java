package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Round;

public interface GameService {
    
    Game startGame(String roomId);

    Game endGame(String gameId);

    Round startRound(String gameId);

    Round endRound(String gameId);
    
}
