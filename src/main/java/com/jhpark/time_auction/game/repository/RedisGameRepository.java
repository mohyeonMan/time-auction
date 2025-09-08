package com.jhpark.time_auction.game.repository;

import java.util.List;

import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Phase;
import com.jhpark.time_auction.game.model.Round;

public class RedisGameRepository implements GameRepository {

    @Override
    public Game deleteGameByGameId(String gameId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Round deleteRoundByRoundId(String roundId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Game getGamesByRoomId(String roomId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Round> getRoundsByGameId(String gameId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Game saveGame(String roomId, int maxTime, int maxRound) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Round saveRound(String gameId, int round) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Game updatePhase(String gameId, Phase phase) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
