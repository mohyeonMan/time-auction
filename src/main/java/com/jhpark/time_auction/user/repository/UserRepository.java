package com.jhpark.time_auction.user.repository;

import com.jhpark.time_auction.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
