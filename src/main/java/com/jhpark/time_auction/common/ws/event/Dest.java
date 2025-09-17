package com.jhpark.time_auction.common.ws.event;

public final class Dest {
  private Dest(){}
  public static final String USER_ACK = "/queue/ack";
  public static final String USER_ERRORS = "/queue/errors";
  public static String userPersonal(String roomId) { return "/queue/room." + roomId + ".personal"; }
  public static String roomEvent(String roomId) { return "/topic/room." + roomId + ".event"; }
  public static String roomState(String roomId) { return "/topic/room." + roomId + ".state"; }
}