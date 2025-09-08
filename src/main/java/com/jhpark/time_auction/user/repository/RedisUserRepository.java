package com.jhpark.time_auction.user.repository;

import com.jhpark.time_auction.user.model.User;

public class RedisUserRepository implements UserRepository {

    @Override
    public User deleteUserBySessionId(String sessionId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean expireSession() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public User saveUser(String nickname) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
