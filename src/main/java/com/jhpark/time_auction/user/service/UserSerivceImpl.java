package com.jhpark.time_auction.user.service;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.user.model.User;

@Service
public class UserSerivceImpl implements UserService {

    @Override
    public boolean keepAlive(String sessionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keepAlive'");
    }

    @Override
    public User joinSession(String sessionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'joinSession'");
    }

    @Override
    public User leftSession(String sessionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leftSession'");
    }
    
}
