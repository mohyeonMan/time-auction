# `타임옥션` 게임 설계안 (v5 - 최종)

`TimeLog`(시작 기록)와 `BidResult`(최종 결과)를 명확히 분리하는 최종 설계안입니다.

## 1. 도메인 모델

### 1.1. `Game`, `Round`, `PlayerState` 모델
*   이전 설계(v4)와 동일합니다.
*   단, `Round` 모델은 `timeLogIds`가 아닌 `bidResultIds`를 저장합니다.

```java
// Round.java 일부
public class Round {
    // ... 기존 필드 ...
    private List<String> bidResultIds; // 이번 라운드에 생성된 BidResult의 ID 목록
}
```

### 1.2. `TimeStartLog` 모델 (신규, 임시 로그)
*   플레이어의 시간 소모 '시작' 이벤트만을 기록하기 위한 임시 객체입니다.
*   **파일**: `src/main/java/com/jhpark/time_auction/record/model/TimeStartLog.java`

```java
package com.jhpark.time_auction.record.model;

// ...
@Getter
@RedisHash("time_start_logs")
public class TimeStartLog {
    @Id private String id; // {roundId}_{roomEntryId}
    private long startTime;
    @TimeToLive private Long ttl = 600L; // end 이벤트가 들어올 때까지만 유효
}
```

### 1.3. `BidResult` 모델 (신규, 최종 결과)
*   플레이어의 라운드별 최종 결과 정보를 모두 기록합니다.
*   **파일**: `src/main/java/com/jhpark/time_auction/record/model/BidResult.java`

```java
package com.jhpark.time_auction.record.model;

// ...
@Getter @Setter
@RedisHash("bid_results")
public class BidResult {
    @Id private String id; // {roundId}_{roomEntryId}
    @Indexed private String roundId;
    @Indexed private String roomEntryId;
    
    private long startTime;
    private long endTime;
    private long consumedTime;
    
    private boolean isWinner = false;
    private long remainingTimeAfterRound;

    @TimeToLive private Long ttl; // 게임이 끝날 때까지 유지
}
```

---

## 2. 서비스 로직 설계 (수정)

### `BidResultService`

*   `createBidResultOnEnd(roundId, entryId, endTime)`:
    1.  `TimeStartLogRepository`에서 `TimeStartLog`를 조회합니다. (없으면 예외)
    2.  `consumedTime`을 계산합니다. (`endTime` - `startTime`)
    3.  `PlayerStateRepository`에서 `PlayerState`를 조회합니다.
    4.  **(핵심 로직)** `consumedTime`이 `playerState.getRemainingTime()`을 초과하지 않는지 검증합니다.
    5.  `playerState.setRemainingTime(playerState.getRemainingTime() - consumedTime)`로 남은 시간을 차감하고, `PlayerStateRepository`에 **즉시 저장**합니다.
    6.  새로운 `BidResult` 객체를 생성합니다. 이때 `remainingTimeAfterRound` 필드에 방금 계산된 `playerState`의 최종 남은 시간을 저장합니다.
    7.  `BidResultRepository`에 새로운 `BidResult`를 저장합니다.
    8.  사용한 `TimeStartLog`를 삭제합니다.
    9.  `Round` 객체의 `bidResultIds` 리스트에 새로운 `bidResult.id`를 추가하고 저장합니다.
    10. 마지막 참여자인지 확인하고, 맞다면 `roundService.settleRound(roundId)`를 호출합니다.

### `RoundService`

*   `settleRound(roundId)`:
    1.  `round.getBidResultIds()`를 이용해 해당 라운드의 모든 `BidResult`를 조회합니다. (이 시점의 `BidResult`들은 `remainingTimeAfterRound`가 이미 확정된 상태입니다.)
    2.  `consumedTime`을 비교하여 승리한 `BidResult`를 찾고, 해당 객체의 `isWinner` 플래그를 `true`로 설정하여 저장합니다.
    3.  승리한 `BidResult`의 `roomEntryId`를 이용하여 `PlayerState`를 찾고, `roundsWon` 카운트를 1 증가시켜 저장합니다.
    4.  `Round`의 상태를 `SETTLED`로 변경하고, `gameService.startNextRound()`를 호출하여 다음 라운드를 준비시킵니다.

### `TimeLogService` (또는 `TimeRecordService`)

*   `recordStartTime(roundId, entryId, startTime)`:
    1.  `TimeStartLog` 객체를 생성하고 Redis에 저장합니다.

### 컨트롤러

*   `@MessageMapping("/round/{roundId}/time/start")`: `timeLogService.recordStartTime()` 호출
*   `@MessageMapping("/round/{roundId}/time/end")`: `bidResultService.createBidResultOnEnd()` 호출