package com.jhpark.time_auction.common.ws.model;

public record Ack<T>(Meta meta, T payload
) {

  public static <T> Ack<T> ok(Meta meta, T payload) {
     return new Ack<>(meta, payload); 
  }
}

