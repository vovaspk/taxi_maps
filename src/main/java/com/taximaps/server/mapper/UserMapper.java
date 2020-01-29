package com.taximaps.server.mapper;

import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.user.UserDto;
import com.taximaps.server.entity.dto.user.UserProfileFormDto;

public interface UserMapper {
    User toUserEntity(UserProfileFormDto userProfileFormDto);
    UserProfileFormDto toUserFormDto(User user);
    UserDto toUserDto (User user);
    User fromDtoToUserEntity (UserDto userDto);
}
