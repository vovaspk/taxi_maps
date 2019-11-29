package com.taximaps.server.mapper.impl;

import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.UserProfileFormDto;
import com.taximaps.server.mapper.UserMapper;
import org.springframework.stereotype.Component;

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
        userProfileFormDto.setName(user.getUserName());
        userProfileFormDto.setEmail(user.getEmail());
        userProfileFormDto.setPassword(user.getPassword());
        return userProfileFormDto;
    }
}
