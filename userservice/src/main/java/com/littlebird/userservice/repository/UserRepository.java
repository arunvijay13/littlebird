package com.littlebird.userservice.repository;

import com.littlebird.userservice.dao.UserInfoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserInfoDAO, String> {

    Optional<UserInfoDAO> findByUsername(String username);
    boolean existsByUsername(String username);
}
