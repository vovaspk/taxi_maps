package com.taximaps.server.controller.rest;

import com.taximaps.server.entity.dto.user.UserDto;
import com.taximaps.server.entity.dto.user.UserProfileFormDto;
import com.taximaps.server.mapper.UserMapper;
import com.taximaps.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserProfileController {

    private UserMapper userMapper;
    private UserService userService;

    @PostMapping("/update")
    public void update(UserProfileFormDto userProfileFormDto){
        userService.updateUser(userMapper.toUserEntity(userProfileFormDto));

    }

    @GetMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserProfile(Authentication authentication){
        return userService.getUserProfile(authentication.getName());
    }
}
