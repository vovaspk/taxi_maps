package com.taximaps.server.mapper.impl;

import com.taximaps.server.entity.Role;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.UserDto;
import com.taximaps.server.entity.dto.UserProfileFormDto;
import com.taximaps.server.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUserEntity(UserProfileFormDto formDataDto) {
        User user = new User();
        user.setUserName(formDataDto.getName());
        user.setEmail(formDataDto.getEmail());
        user.setPassword(formDataDto.getPassword());
        user.setActive(true);
        return user;
    }

    @Override
    public UserProfileFormDto toUserFormDto(User user) {
        UserProfileFormDto userProfileFormDto = new UserProfileFormDto();
        userProfileFormDto.setName(user.getUsername());
        userProfileFormDto.setEmail(user.getEmail());
        userProfileFormDto.setPassword(user.getPassword());
        return userProfileFormDto;
    }

    @Override
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    @Override
    public User fromDtoToUserEntity(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }
}
