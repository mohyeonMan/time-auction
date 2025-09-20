package com.jhpark.time_auction.game.service;

import com.jhpark.time_auction.common.exception.CustomMessageException;
import com.jhpark.time_auction.game.model.Game;
import com.jhpark.time_auction.game.model.GameEntry;
import com.jhpark.time_auction.game.model.GameStatus;
import com.jhpark.time_auction.game.repository.GameEntryRepository;
import com.jhpark.time_auction.game.repository.GameRepository;
import com.jhpark.time_auction.room.service.RoomService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameServiceIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameEntryRepository gameEntryRepository;

    @MockBean
    private RoundService roundService; // GameService는 RoundService에 의존하지만, 이 테스트의 초점은 Game 생성과 GameEntry 초기화이므로 Mock 처리

    @MockBean
    private RoomService roomService; // RoomService는 GameService의 직접적인 의존성이 아니지만, RoomEntryId를 제공하므로 Mock 처리

    @Autowired
    private RedisTemplate<String, Object> redisTemplate; // Redis 데이터 클리닝용

    private static final String TEST_ROOM_ID = "testRoomId";
    private static final List<String> TEST_ROOM_ENTRY_IDS = Arrays.asList("entry1", "entry2", "entry3");

    @BeforeEach
    void setUp() {
        // 테스트 전에 Redis 데이터 클리닝
        redisTemplate.getConnectionFactory().getConnection().flushDb();

        // Mock RoomService의 동작 정의
        when(roomService.getEntriesByRoomId(TEST_ROOM_ID)).thenReturn(null); // 이 테스트에서는 사용되지 않음
    }

    @Test
    void testCreateGameAndPlayerInitialization() {
        // Given
        String roomId = TEST_ROOM_ID;
        List<String> roomEntryIds = TEST_ROOM_ENTRY_IDS;

        // When
        Game createdGame = gameService.createGame(roomId, roomEntryIds);

        // Then
        assertNotNull(createdGame);
        assertNotNull(createdGame.getId());
        assertEquals(roomId, createdGame.getRoomId());
        assertEquals(GameStatus.READY, createdGame.getStatus());
        assertEquals(0, createdGame.getCurrentRound()); // createGame에서 0으로 초기화
        assertEquals(roomEntryIds, createdGame.getRoomEntryIds());

        // Redis에 Game 객체가 저장되었는지 확인
        Optional<Game> foundGame = gameRepository.findById(createdGame.getId());
        assertTrue(foundGame.isPresent());
        assertEquals(createdGame.getId(), foundGame.get().getId());

        // 각 roomEntryId에 대해 GameEntry가 생성되고 저장되었는지 확인
        for (String entryId : roomEntryIds) {
            Optional<GameEntry> foundGameEntry = gameEntryRepository.findByGameIdAndRoomEntryId(createdGame.getId(), entryId);
            assertTrue(foundGameEntry.isPresent(), "GameEntry not found for " + entryId);
            assertEquals(createdGame.getId(), foundGameEntry.get().getGameId());
            assertEquals(entryId, foundGameEntry.get().getRoomEntryId());
            assertEquals(createdGame.getTotalTime(), foundGameEntry.get().getRemainingTime());
            assertEquals(0, foundGameEntry.get().getRoundsWon());
        }

        // 중복 게임 생성 시 예외 발생 확인
        assertThrows(CustomMessageException.class, () -> gameService.createGame(roomId, roomEntryIds));
    }
}