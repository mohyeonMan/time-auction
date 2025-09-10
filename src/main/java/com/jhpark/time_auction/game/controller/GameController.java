package com.jhpark.time_auction.game.controller;

import com.jhpark.time_auction.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
}
