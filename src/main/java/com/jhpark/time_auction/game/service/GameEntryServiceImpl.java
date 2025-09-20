package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.game.model.GameEntry;
import com.jhpark.time_auction.game.repository.GameEntryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameEntryServiceImpl implements GameEntryService {

    private final GameEntryRepository gameEntryRepository;

    @Override
    public GameEntry createGameEntry(String gameId, String roomEntryId, long initialTime) {
        String id = GameEntry.create(gameId, roomEntryId, initialTime).getId(); // ID 생성 로직 재사용
        gameEntryRepository.findById(id).ifPresent(ps -> {
            throw new CustomMessageException("GameEntry already exists for this player in this game.");
        });
        GameEntry newGameEntry = GameEntry.create(gameId, roomEntryId, initialTime);
        return gameEntryRepository.save(newGameEntry);
    }

    @Override
    public GameEntry getGameEntry(String gameId, String roomEntryId) {
        return gameEntryRepository.findByGameIdAndRoomEntryId(gameId, roomEntryId)
                .orElseThrow(() -> new CustomMessageException("GameEntry not found for " + roomEntryId + " in game " + gameId));
    }

    @Override
    public List<GameEntry> getGameEntries(String gameId) {
        return gameEntryRepository.findAllByGameId(gameId);
    }

    @Override
    public GameEntry updateRemainingTime(String gameId, String roomEntryId, long consumedTime) {
        GameEntry gameEntry = getGameEntry(gameId, roomEntryId);
        if (gameEntry.getRemainingTime() < consumedTime) {
            throw new CustomMessageException("남은 시간이 부족합니다. 소모 시간: " + consumedTime + ", 남은 시간: " + gameEntry.getRemainingTime());
        }
        gameEntry.setRemainingTime(gameEntry.getRemainingTime() - consumedTime);
        return gameEntryRepository.save(gameEntry);
    }

    @Override
    public GameEntry updateRoundsWon(String gameId, String roomEntryId, int roundsWon) {
        GameEntry gameEntry = getGameEntry(gameId, roomEntryId);
        gameEntry.setRoundsWon(roundsWon);
        return gameEntryRepository.save(gameEntry);
    }
}
