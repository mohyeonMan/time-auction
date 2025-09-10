package com.jhpark.time_auction.record.controller;

import com.jhpark.time_auction.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
}
