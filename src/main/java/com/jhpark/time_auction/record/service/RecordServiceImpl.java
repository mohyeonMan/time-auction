package com.jhpark.time_auction.record.service;

import com.jhpark.time_auction.record.model.RoundRecord;
import com.jhpark.time_auction.record.model.TimeWallet;
import com.jhpark.time_auction.record.repository.RoundRecordRepository;
import com.jhpark.time_auction.record.repository.TimeWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final TimeWalletRepository timeWalletRepository;
    private final RoundRecordRepository roundRecordRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final long INITIAL_TIME_SECONDS = 300; // 초기 시간 5분

    @Override
    public RoundRecord startRecord(String userId, String roundId, LocalDateTime time) {
        TimeWallet timeWallet = getTimeWallet(userId, extractGameIdFromRoundId(roundId));
        if (timeWallet == null || timeWallet.getTimeLeft() <= 0) {
            throw new IllegalStateException("Not enough time left to start the record.");
        }

        RoundRecord newRecord = new RoundRecord();
        newRecord.setId(UUID.randomUUID().toString());
        newRecord.setUserId(userId);
        newRecord.setRoundId(roundId);
        newRecord.setStartTime(time);

        return roundRecordRepository.save(newRecord);
    }

    @Override
    public RoundRecord endRecord(String userId, String roundId, LocalDateTime time) {
        RoundRecord record = roundRecordRepository.findByUserIdAndRoundId(userId, roundId)
                .orElseThrow(() -> new IllegalStateException("No start record found for this user and round."));

        // 이미 처리된 요청인지 확인 (멱등성)
        if (record.getEndTime() != null) {
            return record;
        }

        record.setEndTime(time);
        long duration = Duration.between(record.getStartTime(), record.getEndTime()).toSeconds();
        record.setDuration(duration);

        // --- Atomic Operation using RedisTemplate ---
        String gameId = extractGameIdFromRoundId(roundId);
        TimeWallet timeWallet = getTimeWallet(userId, gameId);
        String redisKey = "time_wallets:" + timeWallet.getId();
        redisTemplate.opsForHash().increment(redisKey, "timeLeft", -duration);
        // ------------------------------------------

        return roundRecordRepository.save(record);
    }

    @Override
    public void createTimeWallets(String gameId, List<String> userIds) {
        for (String userId : userIds) {
            TimeWallet newWallet = new TimeWallet();
            newWallet.setId(userId + ":" + gameId); // Make ID predictable
            newWallet.setUserId(userId);
            newWallet.setGameId(gameId);
            newWallet.setTimeLeft(INITIAL_TIME_SECONDS);
            newWallet.setRoundWins(0);
            timeWalletRepository.save(newWallet);
        }
    }

    @Override
    public TimeWallet getTimeWallet(String userId, String gameId) {
        String walletId = userId + ":" + gameId;
        return timeWalletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("TimeWallet not found for user: " + userId));
    }

    @Override
    public List<RoundRecord> getRoundResult(String roundId) {
        return roundRecordRepository.findAllByRoundId(roundId);
    }

    private String extractGameIdFromRoundId(String roundId) {
        if (roundId.contains("_")) {
            return roundId.split("_")[0];
        }
        throw new IllegalArgumentException("Invalid roundId format. Expected format: gameId_roundNumber");
    }

    @Override
    public void addWinToUser(String userId, String gameId) {
        TimeWallet timeWallet = getTimeWallet(userId, gameId);
        String redisKey = "time_wallets:" + timeWallet.getId();
        redisTemplate.opsForHash().increment(redisKey, "roundWins", 1);
    }
}
