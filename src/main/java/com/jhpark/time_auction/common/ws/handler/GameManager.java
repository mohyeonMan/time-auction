package com.jhpark.time_auction.common.ws.handler;

import java.time.LocalDateTime;

public interface GameManager {
    
    void startGame(String roomId);

    void endGame(String gameId);

    void getGameResult(String gameId);

    void startRound(String gameId);

    void endRound(String roundId);

    void roundIn(String roundId, String sessionKey);

    void roundOut(String roundId, String sessionKey);

    void timeStart(String roundId, String sessionKey, LocalDateTime sentAt);

    void timeEnd(String roundId, String sessionKey, LocalDateTime sentAt);

}
