package com.taximaps.server.controller.rest;

import com.taximaps.server.entity.Location;
import com.taximaps.server.entity.User;
import com.taximaps.server.entity.dto.user.UserDto;
import com.taximaps.server.entity.dto.user.UserProfileFormDto;
import com.taximaps.server.mapper.UserMapper;
import com.taximaps.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserProfileController {

    private UserMapper userMapper;
    private UserService userService;

    @PostMapping("/update")
    public void update(UserProfileFormDto userProfileFormDto){
        userService.updateUser(userMapper.toUserEntity(userProfileFormDto));

    }

    @PostMapping(value = "/updateUserGeo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserGeolocation(Authentication auth, @RequestBody Location location){
        System.out.println(location.toString());
        userService.updateUserGeo(auth, location);
    }

    @GetMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserProfile(Authentication authentication){
        return userService.getUserProfile(authentication.getName());
    }

    @GetMapping(value = "/getUsersList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsersList(){
        return userService.getUsers();
    }
}
