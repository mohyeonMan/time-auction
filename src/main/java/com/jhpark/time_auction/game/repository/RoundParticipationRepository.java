package com.jhpark.time_auction.game.repository;

import com.jhpark.time_auction.game.model.RoundParticipation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoundParticipationRepository extends CrudRepository<RoundParticipation, String> {
    List<RoundParticipation> findByRoundId(String roundId);
    Optional<RoundParticipation> findByRoundIdAndRoomEntryId(String roundId, String roomEntryId);
    long countByRoundIdAndHasResponded(String roundId, boolean hasResponded);
}
