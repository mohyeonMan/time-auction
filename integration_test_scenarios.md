# 통합 테스트 시나리오

## 1. 게임 생성 및 시작 흐름

*   **시나리오**: 방 생성 -> 플레이어 입장 -> 모든 플레이어 Ready -> 게임 시작 요청 -> 게임 및 첫 라운드 생성 확인
*   **검증**: `Game` 객체 상태(`IN_PROGRESS`, `currentRound=1`), `Round` 객체 상태(`PARTICIPATION_CHOICE`), `GameEntry` 객체 초기화(`remainingTime`, `roundsWon=0`), `RoundParticipation` 객체 생성(`hasResponded=false`, `isParticipating=false`)

## 2. 라운드 참여 및 불참 흐름

*   **시나리오**: 라운드 참여 요청 -> 라운드 불참 요청 -> 모든 플레이어 응답 -> 라운드 시작 (startBidding)
*   **검증**: `RoundParticipation` 객체 상태(`hasResponded`, `isParticipating`), `Round` 객체 상태(`RUNNING`), `CustomMessageException` 발생 여부 (모두 응답하지 않았을 때)

## 3. 시간 소모 및 BidResult 생성 흐름

*   **시나리오**: 라운드 진행 중 -> 플레이어 시간 소모 시작 (`/time/start`) -> 플레이어 시간 소모 종료 (`/time/end`)
*   **검증**: `BidLog` (START/END) 생성 및 삭제, `BidResult` 객체 생성(`consumedTime`, `remainingTimeAfterRound` 정확성), `GameEntry`의 `remainingTime` 정확성, `Round`의 `bidResultIds` 업데이트

## 4. 라운드 정산 및 승자 판정 흐름

*   **시나리오**: 모든 플레이어 시간 소모 완료 -> 라운드 자동 정산 (`settleRound` 호출)
*   **검증**: `Round` 객체 상태(`SETTLED`, `winnerEntryId`), `BidResult`의 `isWinner` 플래그, `GameEntry`의 `roundsWon` 증가, `CustomMessageException` 발생 여부 (라운드 상태가 `RUNNING`이 아닐 때)

## 5. 다음 라운드 진행 흐름

*   **시나리오**: 라운드 정산 완료 -> `GameService.startNextRound` 호출 -> 다음 라운드 생성 및 시작
*   **검증**: `Game` 객체의 `currentRound` 증가, 새로운 `Round` 객체 생성 및 상태(`PARTICIPATION_CHOICE`)

## 6. 게임 종료 및 최종 승자 판정 흐름

*   **시나리오**: 모든 라운드 종료 -> `GameService.endGame` 호출 -> 최종 승자 판정
*   **검증**: `Game` 객체 상태(`FINISHED`), 최종 승자 `GameEntry`의 `roundsWon` 및 `remainingTime` 기반 판정 정확성

## 7. 예외 및 에러 처리

*   **시나리오**: 존재하지 않는 방/라운드/플레이어 ID로 요청, 남은 시간 초과 소모, 이미 응답한 참여자 재응답 등
*   **검증**: `CustomMessageException` 발생 및 적절한 에러 메시지 반환
