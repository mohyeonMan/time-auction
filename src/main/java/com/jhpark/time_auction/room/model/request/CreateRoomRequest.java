package com.jhpark.time_auction.room.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CreateRoomRequest {
    private String roomName;
}
