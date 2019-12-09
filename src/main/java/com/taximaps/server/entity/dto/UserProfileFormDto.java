package com.taximaps.server.entity.dto;

import lombok.Data;

@Data
public class UserProfileFormDto {

    private String name;
    private String email;
    private String password;

    public UserProfileFormDto() {
    }

}
