package com.taximaps.server.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER, DRIVER;

    @Override
    public String getAuthority() {
        return name();
    }
}
