package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.model.RoundStatus;
import com.jhpark.time_auction.game.repository.RoundRepository;
import com.jhpark.time_auction.record.model.BidLog;
import com.jhpark.time_auction.record.model.BidLogType;
import com.jhpark.time_auction.record.model.BidResult;
import com.jhpark.time_auction.record.service.BidLogService;
import com.jhpark.time_auction.record.service.BidResultService;
import com.jhpark.time_auction.user.model.GameEntry;
import com.jhpark.time_auction.user.service.GameEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final RoundRepository roundRepository;
    // private final GameService gameService; // GameService 주입 제거
    private final BidLogService bidLogService; // BidLogService 주입
    private final BidResultService bidResultService;
    private final GameEntryService gameEntryService;
    private final RoundParticipationService roundParticipationService; // RoundParticipationService 주입

    @Override
    public Round createRound(String gameId, int roundNumber, List<String> allGameParticipants) {
        // GameService를 통해 Game 객체 조회는 GameService에서 담당
        // Game game = gameService.getGame(gameId); // GameService 주입 제거로 인한 변경

        Round newRound = Round.create(gameId, roundNumber); // Round.create는 이제 participants를 받지 않음
        roundRepository.save(newRound);

        // 모든 참가자에 대한 RoundParticipation 객체 생성
        roundParticipationService.createAllParticipationsForRound(newRound.getId(), allGameParticipants);

        return newRound;
    }

    @Override
    public void optInToRound(String roundId, String roomEntryId) {
        roundParticipationService.updateParticipation(roundId, roomEntryId, true);
        // TODO: 라운드 참가 이벤트 브로드캐스팅
    }

    @Override
    public void optOutOfRound(String roundId, String roomEntryId) {
        roundParticipationService.updateParticipation(roundId, roomEntryId, false);

        // TODO: 라운드 불참 이벤트 브로드캐스팅
    }

    @Override
    public void startBidding(String roundId) {
        Round round = roundRepository.findById(roundId)
                .orElseThrow(() -> new CustomMessageException("라운드를 찾을 수 없습니다: " + roundId));

        if (round.getStatus() != RoundStatus.PARTICIPATION_CHOICE) {
            throw new CustomMessageException("라운드 상태가 '참여 선택'이 아닙니다.");
        }

        // 모든 참여자가 응답했는지 확인 (GameService에서 totalParticipants를 받아와야 함)
        // if (!roundParticipationService.checkAllResponded(roundId, game.getRoomEntryIds().size())) { // GameService 주입 제거로 인한 변경
        //     throw new CustomMessageException("모든 플레이어가 참여 여부를 결정하지 않았습니다.");
        // }

        round.setStatus(RoundStatus.RUNNING);
        roundRepository.save(round);

        // TODO: 라운드 시작 이벤트 브로드캐스팅 (카운트다운 시작 등)
    }

    @Override
    public void settleRound(String roundId, String gameId) {
        Round round = roundRepository.findById(roundId)
                .orElseThrow(() -> new CustomMessageException("라운드를 찾을 수 없습니다: " + roundId));

        if (round.getStatus() != RoundStatus.RUNNING) {
            throw new CustomMessageException("라운드 상태가 '진행 중'이 아닙니다.");
        }

        // 1. 모든 BidResult 조회 (BidResultService를 통해)
        List<BidResult> bidResults = bidResultService.findByRoundId(roundId);

        // 2. 승자 판정 (가장 많은 시간을 소모한 플레이어)
        Optional<BidResult> winnerBidResult = bidResults.stream()
                .max(Comparator.comparingLong(BidResult::getConsumedTime));

        if (winnerBidResult.isEmpty()) {
            // 참여자가 없거나, 유효한 BidResult가 없는 경우
            round.setStatus(RoundStatus.SETTLED);
            roundRepository.save(round);
            // TODO: 라운드 결과 없음 이벤트 브로드캐스팅
            return;
        }

        BidResult winner = winnerBidResult.get();
        winner.setWinner(true);
        bidResultService.save(winner); // 승자 BidResult 업데이트 (BidResultService를 통해)

        // 3. 승자의 GameEntry 업데이트 (roundsWon 증가)
        // Game game = gameService.getGame(round.getGameId()); // GameService 주입 제거로 인한 변경
        gameEntryService.updateRoundsWon(gameId, winner.getRoomEntryId(), gameEntryService.getGameEntry(gameId, winner.getRoomEntryId()).getRoundsWon() + 1);

        // 4. 라운드 상태 변경 및 다음 라운드 준비
        round.setWinnerEntryId(winner.getRoomEntryId());
        round.setStatus(RoundStatus.SETTLED);
        roundRepository.save(round);

        // TODO: 라운드 결과 브로드캐스팅 (승자, 각자의 소모 시간, 남은 시간 등)

        // 다음 라운드 시작 (GameService에 위임)
        // gameService.startNextRound(round.getGameId()); // GameService 주입 제거로 인한 변경
    }

    @Override
    public void recordBidLogStart(String roundId, String roomEntryId, long timestamp) {
        bidLogService.recordBidLogStart(roundId, roomEntryId, timestamp);
    }

    @Override
    // @Transactional // Redis의 @Transactional은 여러 키에 걸친 복잡한 작업의 완전한 원자성을 보장하지 않음. (MULTI/EXEC 또는 Lua 스크립트 필요)
    public void recordBidLogEnd(String roundId, String roomEntryId, long timestamp, String gameId) {
        // 1. 시작 BidLog 조회 및 삭제 (BidLogService를 통해 접근)
        BidLog startLog = bidLogService.findByRoundIdAndRoomEntryIdAndType(roundId, roomEntryId, BidLogType.START)
                .orElseThrow(() -> new CustomMessageException("시작 기록을 찾을 수 없습니다. 라운드 시작 이벤트가 누락되었거나 이미 종료되었습니다."));
        bidLogService.deleteBidLog(startLog.getId()); // BidLogService를 통해 삭제

        long startTime = startLog.getTimestamp();
        long endTime = timestamp;
        long consumedTime = endTime - startTime;

        // 2. GameEntry 업데이트 (남은 시간 차감) (GameEntryService를 통해 접근)
        Round currentRound = roundRepository.findById(roundId)
                .orElseThrow(() -> new CustomMessageException("라운드를 찾을 수 없습니다: " + roundId));
        // Game game = gameService.getGame(currentRound.getGameId()); // GameService 주입 제거로 인한 변경

        // GameEntryService를 통해 남은 시간 업데이트
        GameEntry gameEntry = gameEntryService.updateRemainingTime(gameId, roomEntryId, consumedTime);

        // 3. BidResult 생성 및 저장 (BidResultService를 통해 접근)
        bidResultService.createBidResult(roundId, roomEntryId, startTime, endTime, consumedTime, gameEntry.getRemainingTime());

        // 4. Round 객체에 bidResultId 추가 및 정산 트리거
        currentRound.getBidResultIds().add(roundId + "_" + roomEntryId); // BidResult의 ID는 {roundId}_{roomEntryId}
        roundRepository.save(currentRound);

        // 모든 참여자가 완료했는지 확인하고, 마지막이라면 라운드 정산 트리거
        if (roundParticipationService.getActualParticipants(roundId).size() == currentRound.getBidResultIds().size()) {
            settleRound(roundId, gameId); // settleRound도 gameId를 받도록 변경
        }
    }
}
