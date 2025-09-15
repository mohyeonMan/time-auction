package com.jhpark.time_auction.game.repository;

import com.jhpark.time_auction.game.model.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, String> {
    List<Game> findAll();

    Optional<Game> findByRoomId(String roomId);
}