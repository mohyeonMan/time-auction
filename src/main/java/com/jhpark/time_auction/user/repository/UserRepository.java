package com.jhpark.time_auction.user.repository;

import com.jhpark.time_auction.user.model.User;

public interface UserRepository {
    
    User saveUser(String nickname);

    User deleteUserBySessionId(String sessionId);

    boolean expireSession();

}
