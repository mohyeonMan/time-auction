package com.jhpark.time_auction.game.service;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Round;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameServiceImpl implements GameService{

    @Override
    public Game startGame(String roomId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

    @Override
    public Game endGame(String gameId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    @Override
    public Round startRound(String gameId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startRound'");
    }

    @Override
    public Round endRound(String gameId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endRound'");
    }
    
}
