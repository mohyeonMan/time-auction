package com.jhpark.time_auction.record.handler;

import com.jhpark.time_auction.common.ws.event.Ack;
import com.jhpark.time_auction.common.ws.event.Dest;
import com.jhpark.time_auction.common.ws.event.ServerEvent;
import com.jhpark.time_auction.common.ws.event.ServerEventType;
import com.jhpark.time_auction.common.ws.handler.MessagePublisher;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.Round;
import com.jhpark.time_auction.game.model.RoundParticipation;
import com.jhpark.time_auction.game.model.RoundStatus;
import com.jhpark.time_auction.game.service.GameService;
import com.jhpark.time_auction.game.service.RoundParticipationService;
import com.jhpark.time_auction.game.service.RoundService;
import com.jhpark.time_auction.record.model.BidLog;
import com.jhpark.time_auction.record.model.BidResult;
import com.jhpark.time_auction.record.service.BidLogService;
import com.jhpark.time_auction.record.service.BidResultService;
import com.jhpark.time_auction.user.model.GameEntry;
import com.jhpark.time_auction.user.service.GameEntryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordEventHandler {

    private final BidLogService bidLogService;
    private final BidResultService bidResultService;
    private final RoundParticipationService roundParticipationService;
    private final GameService gameService;
    private final RoundService roundService;
    private final GameEntryService gameEntryService;
    private final MessagePublisher<ServerEvent> publisher;

    public Ack<?> handleTimeStart(
        String cid, 
        long sentAt, 
        String roundParticipantId
    ) {
        BidLog bidLog = bidLogService.recordBidLogStart(roundParticipantId, sentAt);

        return Ack.ok(cid, bidLog.getId());
    }

    public Ack<?> handleTimeEnd(
        String cid, 
        long sentAt,
        String roundParticipantId
    ) {

        
        

        BidLog startBidLog = bidLogService.getBidLogs(roundParticipantId);
        RoundParticipation roundParticipation = roundParticipationService.getRoundParticipation(roundParticipantId);
        String gameEntryId = roundParticipation.getGameEntryId();
        GameEntry gameEntry = gameEntryService.getGameEntry(gameEntryId);
        String roundId = roundParticipation.getRoundId();
        Round round = roundService.getRound(roundId);

        long consumedTime = sentAt - startBidLog.getTimestamp();
        long remainTime = gameEntry.getRemainingTime() - consumedTime;
        GameEntry consumedEntry = gameEntryService.updateRemainingTime(gameEntryId, remainTime);
        
        // 2. 배팅 결과 저장.
        BidResult result = bidResultService.recordBidResult(roundId, gameEntryId, consumedTime, consumedEntry.getRemainingTime());
        
        // gameEntry의 game.
        Game game = gameService.getGame(gameEntry.getGameId());

        publisher.publishToUser(gameEntry.getSessionId(), Dest.userPersonal(game.getRoomId()), 
            ServerEvent.builder()
                .type(ServerEventType.TIME_END_CONFIRM)
                .cid(cid)
                .clientAt(sentAt)
                .payload(result)
                .build());
        
        endBidSignal(cid, sentAt, game.getRoomId(), round);


        // 클라이언트에게 종료 확인 응답
        return Ack.ok(cid, "Time recording ended.");
    }

    public void endBidSignal(
        String cid, 
        long sentAt, 
        String roomId,
        Round round
    ){

        String roundId = round.getId();
        Set<String> finishedEntryIds = bidResultService.getFinishedGameEntryIds(roundId);

        if (finishedEntryIds.size() == round.getParticipationCounts()) {

            // 1. 가장 많이 배팅한 entry찾아서 increase 해주기./
            BidResult winnersBidResult = bidResultService.getMostConsumedBidResult(roundId);
            RoundParticipation participation = roundParticipationService.getRoundParticipation(winnersBidResult.getRoundParticipantId());
            GameEntry winner = gameEntryService.increaseRoundsWon(participation.getGameEntryId());

            // 2. round 상태변경.
            // 3. 다음라운드 가능하다면 readyNextRound 실행.

            Game game = gameService.getGame(round.getGameId());

            log.info("전원 배팅 완료 : roundId : {}");

            publisher.publish(Dest.roomEvent(roomId), 
                ServerEvent.builder()
                    .type(ServerEventType.ROUND_END_BROADCAST)
                    .cid(cid)
                    .clientAt(sentAt)
                    .payload(null)
                    .build());
            
        }
    }
}