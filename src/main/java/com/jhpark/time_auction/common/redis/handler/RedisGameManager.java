package com.jhpark.time_auction.common.redis.handler;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.bid.model.BidRecord;
import com.jhpark.time_auction.bid.service.BidService;
import com.jhpark.time_auction.common.redis.util.RedisTemplateUtil;
import com.jhpark.time_auction.common.ws.handler.GameManager;
import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.service.GameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisGameManager implements GameManager{

    private final GameService gameService;
    private final BidService bidService;


    @Override
    public void startGame(String roomId) {
        gameService.startGame(roomId);
    }

    @Override
    public void endGame(String gameId) {
        gameService.endGame(gameId);
    }

    @Override
    public void getGameResult(String gameId) {
        throw new UnsupportedOperationException("Unimplemented method 'getGameResult'");
    }

    @Override
    public void startRound(String gameId) {
        Round round = gameService.startRound(gameId);
        
    }

    @Override
    public void endRound(String roundId) {
        Round round = gameService.endRound(roundId);
    }

    @Override
    public void roundIn(String roundId, String sessionKey) {
        Round round = gameService.roundIn(roundId, sessionKey);
    }

    @Override
    public void roundOut(String roundId, String sessionKey) {
        Round round = gameService.roundOut(roundId, sessionKey);
    }

    @Override
    public void timeStart(String roundId, String sessionKey, LocalDateTime sentAt) {
        BidRecord record = bidService.startBid(roundId, sessionKey, sentAt);
    }

    @Override
    public void timeEnd(String roundId, String sessionKey, LocalDateTime sentAt) {
        BidRecord record = bidService.endBid(roundId, sessionKey, sentAt);
    }

}
