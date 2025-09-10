package com.jhpark.time_auction.game.repository;

import java.util.List;

import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Phase;
import com.jhpark.time_auction.game.model.Round;

public interface GameRepository {
    
    Game saveGame(String roomId, int maxTime, int maxRound);

    Game deleteGameByGameId(String gameId);

    Game updatePhase(String gameId, Phase phase);

    Game getGamesByRoomId(String roomId);

    List<Round> getRoundsByGameId(String gameId);

    Round saveRound(String gameId, int round);

    Round deleteRoundByRoundId(String roundId);


}
