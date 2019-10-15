package com.taximaps.server.service;

import com.taximaps.server.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    boolean addUser(User user);
    UserDetails loadUserByUsername(String s);
}
