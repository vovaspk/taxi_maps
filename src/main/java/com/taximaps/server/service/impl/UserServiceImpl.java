package com.taximaps.server.service.impl;

import com.taximaps.server.dao.UserDao;
import com.taximaps.server.domain.Role;
import com.taximaps.server.domain.User;
import com.taximaps.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean addUser(User user){
        User userFromDB = userDao.findByUserName(user.getUserName());

        if(userFromDB != null){
            return false;
        }

        user.setRoles(Collections.singleton(Role.USER));
        userDao.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUserName(s);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }


}
