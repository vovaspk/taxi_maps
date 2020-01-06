package com.taximaps.server.service;

import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    boolean addUser(User user);
    UserDetails loadUserByUsername(String s);
    void save(User user);
    UserDto getUserProfile(String name);
}
