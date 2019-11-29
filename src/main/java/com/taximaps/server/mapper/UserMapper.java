package com.taximaps.server.mapper;

import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.UserProfileFormDto;

public interface UserMapper {
    User toUserEntity(UserProfileFormDto userProfileFormDto);
    UserProfileFormDto toUserFormDto(User user);
}
