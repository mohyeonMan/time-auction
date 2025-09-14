package com.jhpark.time_auction.common.ws.event;

import java.time.Instant;

public record Meta(String cid, String sid, long clientAt, long serverAt) {
    public static Meta of(String cid, String sid, long clientAt) {
     return new Meta(
        cid,
        sid,
        clientAt,
        Instant.now().toEpochMilli()
    );
  }
}
