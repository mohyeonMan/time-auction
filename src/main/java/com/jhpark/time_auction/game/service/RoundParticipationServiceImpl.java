package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.game.model.RoundParticipation;
import com.jhpark.time_auction.game.repository.RoundParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundParticipationServiceImpl implements RoundParticipationService {

    private final RoundParticipationRepository roundParticipationRepository;

    @Override
    public void createAllParticipationsForRound(String roundId, List<String> roomEntryIds) {
        roomEntryIds.forEach(roomEntryId -> {
            RoundParticipation participation = RoundParticipation.create(roundId, roomEntryId);
            roundParticipationRepository.save(participation);
        });
    }

    @Override
    public RoundParticipation updateParticipation(String roundId, String roomEntryId, boolean isParticipating) {
        RoundParticipation participation = roundParticipationRepository.findByRoundIdAndRoomEntryId(roundId, roomEntryId)
                .orElseThrow(() -> new CustomMessageException("참여 정보를 찾을 수 없습니다: " + roomEntryId + " in round " + roundId));

        if (participation.isHasResponded()) {
            throw new CustomMessageException("이미 응답한 참여자입니다: " + roomEntryId);
        }

        participation.setHasResponded(true);
        participation.setParticipating(isParticipating);
        return roundParticipationRepository.save(participation);
    }

    @Override
    public boolean checkAllResponded(String roundId, int totalParticipants) {
        long respondedCount = roundParticipationRepository.countByRoundIdAndHasResponded(roundId, true);
        return respondedCount == totalParticipants;
    }

    @Override
    public List<String> getActualParticipants(String roundId) {
        return roundParticipationRepository.findByRoundId(roundId).stream()
                .filter(RoundParticipation::isParticipating)
                .map(RoundParticipation::getRoomEntryId)
                .collect(Collectors.toList());
    }
}
