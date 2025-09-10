package com.jhpark.time_auction.room.repository;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.common.redis.util.RedisTemplateUtil;
import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRoomRepository implements RoomRepository {

    private final RedisTemplateUtil redisUtil;

    private static final String NS_ROOM  = "room";
    private static final String NS_ENTRY = "entry";

    /**
     * 엔트리 제거 시:
     * - entries 집합에서 entryId 제거
     * - ready 집합에서도 entryId 제거
     * - entry 오브젝트 삭제
     * - 남은 인원 0이면 entries/ready/room까지 삭제
     *
     * KEYS[1] = room:{roomId}:entries   (SET of entryId)
     * KEYS[2] = entry:{entryId}:obj     (VALUE)
     * KEYS[3] = room:{roomId}:obj       (VALUE)
     * KEYS[4] = room:{roomId}:ready     (SET of ready entryId)
     * ARGV[1] = entryId
     *
     * return: 0 == room deleted, >0 == remaining entries count
     */
    private static final String LUA_LEAVE_AND_MAYBE_DELETE = """
        -- remove from entries
        redis.call('SREM', KEYS[1], ARGV[1])
        -- also remove from ready set (if exists)
        if redis.call('EXISTS', KEYS[4]) == 1 then
          redis.call('SREM', KEYS[4], ARGV[1])
        end

        -- delete entry object
        redis.call('DEL', KEYS[2])

        -- how many entries left?
        local size = redis.call('SCARD', KEYS[1])

        if size == 0 then
          -- delete room-related keys
          redis.call('DEL', KEYS[1])   -- entries
          redis.call('DEL', KEYS[3])   -- room obj
          redis.call('DEL', KEYS[4])   -- ready set (if any)
          return 0
        end

        return size
        """;

    /* ================= Room ================= */

    @Override
    public Room saveRoom(String roomName, String creatorId) {
        Room room = Room.create(roomName, creatorId);
        redisUtil.set(roomObjKey(room.getRoomId()), room);
        return room;
    }

    @Override
    public Room deleteRoomByRoomId(String roomId) {
        Room room = redisUtil.get(roomObjKey(roomId), Room.class);
        if (room == null) return null;

        // 남아있는 엔트리/레디셋 포함 삭제
        Set<String> entryIds = redisUtil.sMembers(roomEntriesKey(roomId), String.class);

        if (!entryIds.isEmpty()) {
            List<String> entryKeys = entryIds.stream().map(this::entryObjKey).toList();
            redisUtil.pipeline(rt -> {
                for (String k : entryKeys) rt.delete(k);
                rt.delete(roomEntriesKey(roomId));
                rt.delete(roomReadyKey(roomId));
                rt.delete(roomObjKey(roomId));
                return null;
            });
        } else {
            redisUtil.del(roomEntriesKey(roomId));
            redisUtil.del(roomReadyKey(roomId));
            redisUtil.del(roomObjKey(roomId));
        }
        return room;
    }

    @Override
    public Room updateMasterId(String roomId) {
        Room room = redisUtil.get(roomObjKey(roomId), Room.class);
        if (room == null) return null;

        Set<String> entries = redisUtil.sMembers(roomEntriesKey(roomId), String.class);
        if (entries.isEmpty()) return room;

        // 임의 엔트리 하나의 sessionId를 master로
        String pick = entries.iterator().next();
        RoomEntry re = redisUtil.get(entryObjKey(pick), RoomEntry.class);
        if (re != null) {
            Room updated = new Room(room.getRoomId(), room.getRoomName(),
                                    re.getSessionId(), room.getCreatedAt());
            redisUtil.set(roomObjKey(roomId), updated);
            return updated;
        }
        return room;
    }

    /* ================= Entry ================= */

    @Override
    public RoomEntry saveRoomEntry(String roomId, String sessionId) {
        RoomEntry entry = RoomEntry.create(roomId, sessionId);
        // entry 저장
        redisUtil.set(entryObjKey(entry.getRoomEntryId()), entry);
        // room entries set에 등록
        redisUtil.sAdd(roomEntriesKey(roomId), entry.getRoomEntryId());
        return entry;
    }

    @Override
    public RoomEntry deleteRoomEntryByRoomEntryId(String roomEntryId) {
        RoomEntry entry = redisUtil.get(entryObjKey(roomEntryId), RoomEntry.class);
        if (entry == null) return null;

        String roomId = entry.getRoomId();

        Integer left = redisUtil.evalLua(
            LUA_LEAVE_AND_MAYBE_DELETE,
            List.of(roomEntriesKey(roomId), entryObjKey(roomEntryId), roomObjKey(roomId), roomReadyKey(roomId)),
            List.of(roomEntryId),
            Integer.class
        );
        log.debug("leave entryId={}, remaining={}", roomEntryId, left);
        return entry;
    }

    @Override
    public List<RoomEntry> getRoomEntriesByRoomId(String roomId) {
        Set<String> ids = redisUtil.sMembers(roomEntriesKey(roomId), String.class);
        if (ids.isEmpty()) return List.of();

        List<String> keys = ids.stream().map(this::entryObjKey).toList();
        List<Object> raw = redisUtil.pipeline(rt -> {
            for (String k : keys) rt.opsForValue().get(k);
            return null;
        });

        return raw.stream()
                  .filter(o -> o instanceof RoomEntry)
                  .map(o -> (RoomEntry) o)
                  .collect(Collectors.toList());
    }

    @Override
    public RoomEntry getEntryById(String roomEntryId) {
        return redisUtil.get(entryObjKey(roomEntryId), RoomEntry.class);
    }

    /* ================= Ready state ================= */

    @Override
    public boolean ready(String roomId, String roomEntryId) {
        long added = redisUtil.sAdd(roomReadyKey(roomId), roomEntryId);
        return added > 0;
    }

    @Override
    public boolean unready(String roomId, String roomEntryId) {
        long removed = redisUtil.sRem(roomReadyKey(roomId), roomEntryId);
        return removed > 0;
    }

    @Override
    public boolean isAllReady(String roomId) {
        long total = redisUtil.sCard(roomEntriesKey(roomId));
        if (total <= 0) return false; // nobody -> not ready to start
        long ready = redisUtil.sCard(roomReadyKey(roomId));
        return ready == total;
    }

    /* ================= Key helpers ================= */

    private String roomObjKey(String roomId) {
        return redisUtil.key(NS_ROOM, roomId, "obj");        // room:{roomId}:obj
    }

    private String roomEntriesKey(String roomId) {
        return redisUtil.key(NS_ROOM, roomId, "entries");    // room:{roomId}:entries (SET of entryId)
    }

    private String roomReadyKey(String roomId) {
        return redisUtil.key(NS_ROOM, roomId, "ready");      // room:{roomId}:ready (SET of ready entryId)
    }

    private String entryObjKey(String entryId) {
        return redisUtil.key(NS_ENTRY, entryId, "obj");      // entry:{entryId}:obj
    }
}
