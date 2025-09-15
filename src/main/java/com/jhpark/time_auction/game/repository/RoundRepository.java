package com.jhpark.time_auction.game.repository;

import com.jhpark.time_auction.game.model.Round;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoundRepository extends CrudRepository<Round, String> {
    List<Round> findAll();

    List<Round> findByGameId(String gameId);
}
