package com.taximaps.server.service;

import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.user.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    boolean addUser(User user);
    void updateUser(User user);
    UserDetails loadUserByUsername(String s);
    UserDto getUserProfile(String name);
    User getUserById(Long id);
    List<User> getUsers();
    void updateUserGeo(Authentication auth, Location location);
}
