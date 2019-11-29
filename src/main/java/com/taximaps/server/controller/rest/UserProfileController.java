package com.taximaps.server.controller.rest;

import com.taximaps.server.entity.dto.UserProfileFormDto;
import com.taximaps.server.mapper.UserMapper;
import com.taximaps.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/update")
    public void update(UserProfileFormDto userProfileFormDto){
        userService.save(userMapper.toUserEntity(userProfileFormDto));

    }
}
