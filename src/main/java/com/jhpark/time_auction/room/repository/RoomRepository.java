package com.jhpark.time_auction.room.repository;

import com.jhpark.time_auction.room.model.Room;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, String> {
    // Basic CRUD methods are inherited from CrudRepository

    List<Room> findAll();

}
