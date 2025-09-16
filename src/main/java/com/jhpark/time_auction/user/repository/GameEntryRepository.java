package com.jhpark.time_auction.user.repository;

import com.jhpark.time_auction.user.model.GameEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;


public interface GameEntryRepository extends CrudRepository<GameEntry, String> {
    Optional<GameEntry> findByGameIdAndRoomEntryId(String gameId, String roomEntryId);
    List<GameEntry> findAllByGameId(String gameId);
}