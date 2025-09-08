package com.jhpark.time_auction.user.service;

import com.jhpark.time_auction.user.model.User;

public interface UserService {
    
    boolean keepAlive(String sessionId);

    User joinSession(String sessionId);

    User leftSession(String sessionId);


}
