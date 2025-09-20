package com.jhpark.time_auction.game.repository;

import org.springframework.data.repository.CrudRepository;

import com.jhpark.time_auction.game.model.GameEntry;

import java.util.Optional;
import java.util.List;


public interface GameEntryRepository extends CrudRepository<GameEntry, String> {
    Optional<GameEntry> findByGameIdAndRoomEntryId(String gameId, String roomEntryId);
    List<GameEntry> findAllByGameId(String gameId);
}