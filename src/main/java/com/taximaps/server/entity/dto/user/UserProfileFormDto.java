package com.taximaps.server.entity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileFormDto {

    private String name;
    private String email;
    private String password;


}
