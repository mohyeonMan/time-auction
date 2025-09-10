package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.repository.GameRepository;
import com.jhpark.time_auction.record.model.RoundRecord;
import com.jhpark.time_auction.record.model.TimeWallet;
import com.jhpark.time_auction.record.service.RecordService;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final RoomService roomService;
    private final RecordService recordService;
    private final TaskScheduler scheduler;

    private static final int ROUND_DURATION_SECONDS = 60;
    private static final int PARTICIPATION_CHOICE_SECONDS = 3;
    private static final int MAX_ROUNDS = 5;

    @Override
    public Game startGame(String roomId) {
        List<RoomEntry> participants = roomService.getReadyUsers(roomId);
        if (participants.size() < 2) {
            throw new IllegalStateException("Cannot start game with less than 2 ready players.");
        }

        Game newGame = new Game(UUID.randomUUID().toString(), roomId, 0);
        gameRepository.save(newGame);

        List<String> userIds = participants.stream().map(RoomEntry::getSessionId).collect(Collectors.toList());
        recordService.createTimeWallets(newGame.getId(), userIds);

        log.info("Game started: {}", newGame.getId());
        startRound(newGame.getId());
        return newGame;
    }

    @Override
    public Round startRound(String gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        int nextRoundNumber = game.getCurrentRound() + 1;
        String roomId = game.getRoomId();

        roomService.getEntriesByRoomId(roomId).forEach(entry -> roomService.setParticipation(roomId, entry.getSessionId(), false));
        log.info("Participation phase started for round {} of game {}", nextRoundNumber, gameId);

        scheduler.schedule(() -> {
            List<RoomEntry> allEntries = roomService.getEntriesByRoomId(roomId);
            allEntries.stream()
                .filter(e -> !e.isParticipating() && recordService.getTimeWallet(e.getSessionId(), gameId).getTimeLeft() > 0)
                .forEach(e -> roomService.setParticipation(roomId, e.getSessionId(), true));

            game.setCurrentRound(nextRoundNumber);
            gameRepository.save(game);

            log.info("Round {} starting for game: {}", nextRoundNumber, gameId);
            scheduler.schedule(() -> endRound(gameId), Instant.now().plusSeconds(ROUND_DURATION_SECONDS));

        }, Instant.now().plusSeconds(PARTICIPATION_CHOICE_SECONDS));

        return new Round(gameId + "_" + nextRoundNumber, gameId, nextRoundNumber);
    }

    @Override
    public Round endRound(String gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        String roundId = game.getId() + "_" + game.getCurrentRound();

        List<RoundRecord> records = recordService.getRoundResult(roundId);

        RoundRecord winnerRecord = records.stream()
                .max(Comparator.comparing(RoundRecord::getDuration))
                .orElse(null);

        if (winnerRecord != null) {
            String winnerId = winnerRecord.getUserId();
            recordService.addWinToUser(winnerId, gameId);
            log.info("Round {} winner: {}", game.getCurrentRound(), winnerId);
        }

        if (game.getCurrentRound() >= MAX_ROUNDS) {
            endGame(gameId);
        }
        else {
            startRound(gameId);
        }
        return new Round(roundId, gameId, game.getCurrentRound());
    }

    @Override
    public Game endGame(String gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        log.info("Game ended: {}", gameId);
        // The handler will be responsible for calculating and broadcasting the final winner
        return game;
    }
}