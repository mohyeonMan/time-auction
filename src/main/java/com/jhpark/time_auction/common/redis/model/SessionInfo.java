package com.jhpark.time_auction.common.redis.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SessionInfo {
    String sessionKey;
    String nodeId;

    public SessionInfo(String sessionKey, String nodeId){
        this.sessionKey = sessionKey;
        this.nodeId = nodeId;
    }
}
