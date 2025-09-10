package com.jhpark.time_auction.room.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jhpark.time_auction.room.model.Room;
import com.jhpark.time_auction.room.model.RoomEntry;
import com.jhpark.time_auction.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    /** 방 생성 + 방장 자동 입장 */
    @Override
    public Room createRoom(String roomName, String sessionKey) {
        Room room = roomRepository.saveRoom(roomName, sessionKey);
        roomRepository.saveRoomEntry(room.getRoomId(), sessionKey);
        return room;
    }

    /** 방 삭제 (엔트리/레디셋 포함) */
    @Override
    public Room deleteRoom(String roomId) {
        return roomRepository.deleteRoomByRoomId(roomId);
    }

    /** 방의 엔트리 목록 조회 */
    @Override
    public List<RoomEntry> getEntriesByRoomId(String roomId) {
        return roomRepository.getRoomEntriesByRoomId(roomId);
    }

    /** 전원 준비 여부 */
    @Override
    public boolean isAllReadyToPlay(String roomId) {
        return roomRepository.isAllReady(roomId);
    }

    /** 방 입장: 같은 세션이 이미 있으면 그대로 반환, 없으면 새로 생성 */
    @Override
    public RoomEntry joinRoom(String roomId, String sessionKey) {
        List<RoomEntry> entries = roomRepository.getRoomEntriesByRoomId(roomId);
        for (RoomEntry e : entries) {
            if (sessionKey.equals(e.getSessionId())) {
                return e; // 이미 입장 중
            }
        }
        return roomRepository.saveRoomEntry(roomId, sessionKey);
    }

    /** 방 퇴장: 해당 세션의 엔트리를 찾아 제거. 마지막이면 방까지 삭제(Repo의 Lua가 처리). */
    @Override
    public RoomEntry leftRoom(String roomId, String sessionKey) {
        // 1) 내 엔트리 찾기
        RoomEntry target = null;
        List<RoomEntry> before = roomRepository.getRoomEntriesByRoomId(roomId);
        for (RoomEntry e : before) {
            if (sessionKey.equals(e.getSessionId())) {
                target = e;
                break;
            }
        }
        if (target == null) return null;

        // 2) 엔트리 삭제 (Repo: entries/ready 정리 + 마지막이면 room 삭제까지 원자 처리)
        RoomEntry removed = roomRepository.deleteRoomEntryByRoomEntryId(target.getRoomEntryId());

        // 3) 남아있다면(방이 아직 존재한다면) 마스터 재지정 시도
        //    - 기존 마스터가 떠났는지 판별하려면 Room 조회가 필요한데,
        //      Repository에 getRoomById가 없다면 안전하게 "항상 재선정"으로 처리
        //      (같은 마스터가 유지될 수도 있고, 재선정되어도 큰 부작용 없게 설계)
        List<RoomEntry> after = roomRepository.getRoomEntriesByRoomId(roomId);
        if (!after.isEmpty()) {
            roomRepository.updateMasterId(roomId);
        }

        return removed;
    }

    @Override
    public boolean ready(String roomEntryId) {
        RoomEntry entry = roomRepository.getEntryById(roomEntryId);
        if (entry == null) return false;
        return roomRepository.ready(entry.getRoomId(), roomEntryId);
    }

    @Override
    public boolean unready(String roomEntryId) {
        RoomEntry entry = roomRepository.getEntryById(roomEntryId);
        if (entry == null) return false;
        return roomRepository.unready(entry.getRoomId(), roomEntryId);
    }
}
