package com.jhpark.time_auction.game.model;

import java.util.List;

import com.jhpark.time_auction.user.model.GameEntry;

import lombok.Getter;

@Getter
public class GameInfo {
    private Game game;
    private List<GameEntry> gameEntryId;
}