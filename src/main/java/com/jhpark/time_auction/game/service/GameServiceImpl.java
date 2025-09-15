package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.GameStatus;
import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.repository.GameRepository;
import com.jhpark.time_auction.user.model.GameEntry;
import com.jhpark.time_auction.user.service.GameEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final RoundService roundService;
    private final GameEntryService gameEntryService;

    @Override
    public Game createGame(String roomId, List<String> roomEntryIds) {
        // 이미 해당 방에서 진행중인 게임이 있는지 확인
        gameRepository.findByRoomId(roomId).ifPresent(game -> {
            throw new CustomMessageException("이미 게임이 진행중인 방입니다.");
        });

        Game newGame = Game.create(roomId, roomEntryIds);
        Game savedGame = gameRepository.save(newGame);

        // 모든 참가자에 대한 GameEntry 초기화
        roomEntryIds.forEach(roomEntryId -> {
            gameEntryService.createGameEntry(savedGame.getId(), roomEntryId, savedGame.getTotalTime());
        });

        return savedGame;
    }

    @Override
    public Game getGame(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomMessageException("게임을 찾을 수 없습니다: " + gameId));
    }

    @Override
    public void startGame(String gameId) {
        Game game = getGame(gameId);

        if (game.getStatus() != GameStatus.READY) {
            throw new CustomMessageException("게임이 시작될 준비가 되지 않았습니다.");
        }

        game.setStatus(GameStatus.IN_PROGRESS);
        game.setCurrentRound(1); // 첫 라운드 시작
        gameRepository.save(game);

        // 첫 라운드 생성 및 시작
        Round newRound = roundService.createRound(game.getId(), game.getCurrentRound());
        roundService.startBidding(newRound.getId());

        // TODO: 게임 시작 이벤트 브로드캐스팅
    }

    @Override
    public void startNextRound(String gameId) {
        Game game = getGame(gameId);

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new CustomMessageException("게임이 진행 중이 아닙니다.");
        }

        if (game.getCurrentRound() >= game.getTotalRounds()) {
            endGame(gameId); // 모든 라운드 종료 시 게임 종료
            return;
        }

        game.setCurrentRound(game.getCurrentRound() + 1);
        gameRepository.save(game);

        // 다음 라운드 생성 및 시작
        Round newRound = roundService.createRound(game.getId(), game.getCurrentRound());
        roundService.startBidding(newRound.getId());

        // TODO: 다음 라운드 시작 이벤트 브로드캐스팅
    }

    @Override
    public void endGame(String gameId) {
        Game game = getGame(gameId);

        if (game.getStatus() == GameStatus.FINISHED) {
            throw new CustomMessageException("이미 종료된 게임입니다.");
        }

        game.setStatus(GameStatus.FINISHED);
        gameRepository.save(game);

        // 최종 승자 판정
        List<GameEntry> gameEntries = game.getRoomEntryIds().stream()
                .map(roomEntryId -> gameEntryService.getGameEntry(gameId, roomEntryId))
                .collect(Collectors.toList());

        Optional<GameEntry> finalWinner = gameEntries.stream()
                .max(Comparator.comparingInt(GameEntry::getRoundsWon)
                        .thenComparingLong(GameEntry::getRemainingTime));

        if (finalWinner.isPresent()) {
            // TODO: 최종 승자 정보 저장 또는 브로드캐스팅
        }

        // TODO: 게임 종료 이벤트 브로드캐스팅
    }
}