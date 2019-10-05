package com.taximaps.server.dao;

import com.taximaps.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUserName (String username);
}
