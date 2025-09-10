package com.jhpark.time_auction.common.redis.handler;

import com.jhpark.time_auction.common.ws.handler.GameManager;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.record.service.RecordService;
import com.jhpark.time_auction.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisGameManager implements GameManager {
    
    private final GameService gameService;
    private final RecordService recordService;
    private final RoomService roomService;

    @Override
    public void startGame(String roomId) {
        log.info("Starting game for room: {}", roomId);
        gameService.createGame(roomId);
    }

    @Override
    public void endGame(String gameId) {
        log.info("Ending game: {}", gameId);
        // Implementation for ending a game
    }

    @Override
    public void getGameResult(String gameId) {
        log.info("Getting game result for game: {}", gameId);
        // Implementation for getting game result
    }

    @Override
    public void startRound(String gameId) {
        log.info("Starting round for game: {}", gameId);
        gameService.startNewRound(gameId);
    }

    @Override
    public void endRound(String roundId) {
        log.info("Ending round: {}", roundId);
        // Implementation for ending a round
    }

    @Override
    public void roundIn(String roundId, String sessionKey) {
        log.info("Session {} entering round {}", sessionKey, roundId);
        // Implementation for a user entering a round
    }

    @Override
    public void roundOut(String roundId, String sessionKey) {
        log.info("Session {} leaving round {}", sessionKey, roundId);
        // Implementation for a user leaving a round
    }

    @Override
    public void timeStart(String roundId, String sessionKey, LocalDateTime sentAt) {
        log.info("Time start for session {} in round {} at {}", sessionKey, roundId, sentAt);
        recordService.startRecording(roundId, sessionKey, sentAt);
    }

    @Override
    public void timeEnd(String roundId, String sessionKey, LocalDateTime sentAt) {
        log.info("Time end for session {} in round {} at {}", sessionKey, roundId, sentAt);
        recordService.endRecording(roundId, sessionKey, sentAt);
    }
}