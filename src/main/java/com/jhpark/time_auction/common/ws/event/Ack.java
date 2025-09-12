package com.jhpark.time_auction.common.ws.event;

public record Ack<T>(String cid, T payload) {
  public static <T> Ack<T> ok(String cid, T payload) { return new Ack<>(cid, payload); }
}