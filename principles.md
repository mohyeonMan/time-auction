# 공통 설계 원칙

## 2.1. 도메인 모델링 원칙 (Spring Data Redis)

1.  **`@RedisHash`를 통한 클래스 매핑**:
    *   모든 도메인 모델은 `@RedisHash("keyspace")` 어노테이션을 사용하여 Redis의 Hash 구조에 매핑합니다.
    *   Keyspace 이름은 복수형(e.g., "rooms", "room_entries")을 사용합니다.

2.  **문자열 기반 ID 사용**:
    *   Primary Key는 `@Id` 어노테이션을 사용하며, 타입은 `String`으로 지정합니다.
    *   ID 값은 `UUID.randomUUID()` 등을 활용하여 충돌이 방지되는 고유한 값으로 생성합니다. (예: `room_` + UUID)

3.  **2차 인덱싱을 위한 `@Indexed`**:
    *   데이터 조회 시 `where` 절의 역할을 하는 필드에는 `@Indexed` 어노테이션을 추가합니다.
    *   이를 통해 `Repository` 계층에서 `findBy{FieldName}`과 같은 메서드를 자동으로 생성할 수 있습니다. (예: `findByRoomId`)

4.  **데이터 자동 소멸을 위한 `@TimeToLive`**:
    *   모든 도메인 모델은 `@TimeToLive` 어노테이션을 가진 `long` 타입의 필드를 포함하여 데이터의 유효시간(초 단위)을 관리합니다.
    *   이는 게임 종료 후 또는 비정상 종료 시 남는 데이터를 자동으로 정리하는 데 핵심적인 역할을 합니다.

5.  **정적 팩토리 메서드(Static Factory Method) 활용**:
    *   객체 생성의 복잡성을 캡슐화하고 일관성을 유지하기 위해 `create()`와 같은 정적 팩토리 메서드를 사용합니다.
    *   이 메서드 내에서 ID 생성, 타임스탬프 기록, TTL 설정 등 객체 초기화에 필요한 모든 로직을 처리합니다.

6.  **Lombok을 통한 Boilerplate 최소화**:
    *   `@Getter`, `@Setter`, `@ToString`, `@NoArgsConstructor`, `@AllArgsConstructor` 등의 Lombok 어노테이션을 적극적으로 사용하여 코드 가독성과 유지보수성을 높입니다.


## 2.2. 서비스 레이어 역할 정의 (비즈니스 로직)

1.  **핵심 비즈니스 로직 전담**:
    *   서비스 레이어는 도메인의 상태를 변경하고, 비즈니스 규칙을 검증하는 **유일한** 창구입니다.
    *   `EventHandler`나 `Controller`는 비즈니스 로직을 직접 수행하지 않고, 반드시 서비스 레이어에 위임해야 합니다.

2.  **Repository와 상호작용**:
    *   하나의 비즈니스 로직을 수행하기 위해 여러 Repository를 호출하고, 데이터의 정합성을 맞추는 역할을 담당합니다.

3.  **예외 처리를 통한 규칙 강제**:
    *   비즈니스 규칙에 위배되는 상황(예: 존재하지 않는 방에 입장, 중복 입장)이 발생하면, `CustomMessageException`과 같은 명시적인 예외를 발생시켜 로직의 흐름을 중단시킵니다.

4.  **순수한(Pure) 입출력**:
    *   서비스 레이어는 WebSocket이나 HTTP와 같은 외부 기술에 의존하지 않습니다.
    *   메서드의 파라미터는 도메인과 관련된 순수한 데이터(예: `roomId`, `nickname`)이며, 반환 값 역시 순수한 도메인 객체(예: `RoomEntry`) 또는 기본 타입입니다.

5.  **인터페이스 기반 구현**:
    *   `RoomService`라는 인터페이스를 정의하고, `RoomServiceImpl`에서 이를 구현합니다. 이는 향후 테스트 코드 작성 및 기능 확장에 유연한 구조를 제공합니다.


## 2.3. 이벤트 기반 통신 패턴

1.  **CQRS 유사 패턴 적용**:
    *   상태를 변경하는 행위(Command)와 상태를 조회하는 행위(Query)의 처리 흐름을 분리합니다.
    *   **Command**: `Controller` -> `EventHandler` -> `Service` -> `Repository`
    *   **Query**: `Controller` -> `Service` -> `Repository` (주석 처리된 코드 기준)

2.  **`EventHandler`의 오케스트레이션**:
    *   `EventHandler`는 Command 처리 흐름의 지휘자(Orchestrator) 역할을 합니다.
    *   `Service`를 호출하여 상태 변경을 위임하고, 그 결과를 `ServerEvent`로 가공하여 `Publisher`에게 전달하는 책임을 가집니다.

3.  **`MessagePublisher`를 통한 발행 추상화**:
    *   `EventHandler`는 `SimpMessageSendingOperations`를 직접 사용하지 않고, `MessagePublisher`라는 래퍼(Wrapper)를 통해 이벤트를 발행합니다. 이는 메시징 기술과의 결합도를 낮춥니다.

4.  **토픽(Topic) 기반 브로드캐스팅**:
    *   상태 변경 이벤트는 특정 주체(예: `room`)와 관련된 토픽(예: `/topic/rooms/{roomId}`)으로 발행됩니다.
    *   이를 통해 해당 주체에 관심 있는 모든 클라이언트가 이벤트를 구독하여 실시간으로 상태 동기화를 할 수 있습니다.

5.  **`Ack`를 통한 즉각적인 피드백**:
    *   명령을 요청한 사용자에게는 `@SendToUser` 또는 유사한 방식을 통해 `Ack` (Acknowledgement) 메시지를 즉시 반환합니다.
    *   이는 브로드캐스팅되는 `ServerEvent`와 별개로, 요청자에게 자신의 요청이 성공적으로 처리되었음을 보장하여 사용자 경험(UX)을 향상시킵니다.


## 2.4. WebSocket 엔드포인트 설계 (STOMP)

1.  **`@MessageMapping` 기반 Command 처리**:
    *   클라이언트가 서버의 상태 변경을 요청하는 모든 통신은 `@MessageMapping` 어노테이션이 붙은 메서드를 통해 처리합니다.

2.  **RESTful 스타일 URL 설계**:
    *   엔드포인트 주소는 행위보다는 자원(Resource) 중심으로 설계합니다. (예: `/room/create`, `/room/{roomId}/join`)
    *   URL 경로 상의 변수는 `@DestinationVariable`을 사용하여 파라미터로 받습니다.

3.  **인증 정보 강제**:
    *   모든 엔드포인트는 `java.security.Principal` 객체를 파라미터로 받아, 요청자가 인증된 사용자인지 확인합니다. 이를 통해 모든 요청을 특정 사용자와 연결합니다.

4.  **메타데이터를 위한 Custom Header 활용**:
    *   메시지 추적 및 응답 연동을 위해 `x-msg-id`(Correlation ID), `x-sent-at`(클라이언트 전송 시각) 등의 Custom Header를 사용하고 `@Header`로 파라미터에 매핑합니다.

5.  **`@Payload`를 통한 데이터 전달**:
    *   요청에 필요한 핵심 데이터는 메시지의 Body에 담아 보내고, 서버에서는 `@Payload` 어노테이션으로 받습니다.

6.  **Controller의 역할 최소화**:
    *   Controller는 STOMP 메시지로부터 필요한 데이터(Principal, Header, Payload 등)를 추출하여 `EventHandler`에 전달하는 역할만 수행합니다. Controller 자체에는 어떠한 비즈니스 로직도 포함하지 않습니다.