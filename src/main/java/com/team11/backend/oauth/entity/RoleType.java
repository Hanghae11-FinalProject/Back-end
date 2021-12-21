package com.team11.backend.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한");


    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return USER;
    }
}