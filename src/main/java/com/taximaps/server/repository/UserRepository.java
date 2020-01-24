package com.taximaps.server.repository;

import com.taximaps.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName (String username);

    User findUserById(Long id);
}
