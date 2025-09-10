package com.jhpark.time_auction.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("users")
public class User {
    @Id
    String sessionId;
    String nickname;
    String nodeId;
}
