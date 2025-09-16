package com.jhpark.time_auction.game.service;

import java.util.List;

import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.GameInfo;
import com.jhpark.time_auction.game.model.GameResult;
import com.jhpark.time_auction.game.model.GameStatus;
import com.jhpark.time_auction.game.model.Round;

public interface GameService {

    /*
     * ready 한 roomEntry들을 불러와서, gameEntry로 추가하고, 게임이 시작되었음을 알림.
     * 각 game과 사용자의 gameEntryId 를 convertAndSentToUser 로 전달.
     * next : getGameEntry
     */
    GameInfo startGame(String roomId, Integer totalRound, Integer totalTime, List<String> roomEntryIds);

    GameStatus getGameStatus(String gameId);            //현재 게임상태
    
    Game getGame(String gameId);                        //게임 조회

    /*
     * before : startGame
     * 새로운 round를 생성하고, currentRound ++, round시작.
     * next : roundIn, roundOut
     */
    Round readyNextRound(String gameId);                //다음 라운드 시작

    Game endGame(String gameId);                        //게임 종료

    GameResult getGameResult(String gameId);                  //게임 결과 조회

}