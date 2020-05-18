package com.taximaps.server.service.impl;

import com.taximaps.server.entity.Role;
import com.taximaps.server.entity.User;
import com.taximaps.server.mapper.LocationMapper;
import com.taximaps.server.mapper.impl.UserMapperImpl;
import com.taximaps.server.repository.LocationRepository;
import com.taximaps.server.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String USER_NAME = "userName";
    private static final String WRONG_USER_NAME = "qwerty123";
    private static final String PASSWORD = "password";
    private static final long USER_ID = 1L;
    private static final String EMAIL = "email";
    private static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapperImpl userMapper;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private LocationMapper locationMapper;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, locationRepository, locationMapper);
    }


    @Test
    void testAddUser() {
        User registeredUser = getUser();
        User notRegisteredUser = null;
        when(userRepository.findByUserName(USER_NAME)).thenReturn(notRegisteredUser);
        when(passwordEncoder.encode(registeredUser.getPassword())).thenReturn(ENCODED_PASSWORD);
        userService.addUser(registeredUser);
        verify(userRepository).save(registeredUser);
    }

    @Test
    public void testAddExistingUserShouldReturnFalse(){

    }

    @Test
    public void updateUser(){}


    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(getUser());
        UserDetails userDetails = userService.loadUserByUsername(USER_NAME);
        Assertions.assertNotNull(userDetails);
    }

    @Test
    void testLoadByInvalidUseNameShouldThrowException() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(WRONG_USER_NAME));
    }

    @Test
    void save() {
//        User user = getUser();
//        when(userRepository.findByUserName(USER_NAME)).thenReturn(user);
//        userService.updateUser(user);
//        verify(userRepository).save(user);
    }

    @Test
    void testGetUserProfile() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(getUser());
        userService.getUserProfile(USER_NAME);
    }

    @Test
    void testGetUserById() {
        userService.getUserById(USER_ID);
        verify(userRepository).findUserById(USER_ID);
    }

    private User getUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setPassword(PASSWORD);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }
}