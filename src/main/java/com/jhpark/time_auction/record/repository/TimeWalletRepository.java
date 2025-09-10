package com.jhpark.time_auction.record.repository;

import com.jhpark.time_auction.record.model.TimeWallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TimeWalletRepository extends CrudRepository<TimeWallet, String> {
    Optional<TimeWallet> findByUserIdAndGameId(String userId, String gameId);
}
