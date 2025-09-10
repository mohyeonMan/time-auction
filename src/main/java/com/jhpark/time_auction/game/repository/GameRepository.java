package com.jhpark.time_auction.game.repository;

import com.jhpark.time_auction.game.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, String> {
}
