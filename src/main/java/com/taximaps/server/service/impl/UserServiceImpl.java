package com.taximaps.server.service.impl;

import com.taximaps.server.entity.Role;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.user.UserDto;
import com.taximaps.server.mapper.UserMapper;
import com.taximaps.server.repository.UserRepository;
import com.taximaps.server.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    public boolean addUser(User user){
        User userFromDB = userRepository.findByUserName(user.getUsername());

        if(userFromDB != null){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        if (!user.getRoles().contains(Role.DRIVER)) {
            user.setRoles(Collections.singleton(Role.USER));
        }
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s);

        if(user == null){
            throw new UsernameNotFoundException("User not found with name: " + s);
        }

        return user;
    }

    @Override
    public void updateUser(User user) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User userFromDb = userRepository.findByUserName(username);

        if(userFromDb != null){
            userFromDb.setUserName(user.getUsername());
            userFromDb.setEmail(user.getEmail());
            //crypt password
            userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("updating user: {}", user.getEmail());
            userRepository.save(userFromDb);
        }

    }

    @Override
    public UserDto getUserProfile(String name) {
        User user = userRepository.findByUserName(name);
        return userMapper.toUserDto(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }


}
