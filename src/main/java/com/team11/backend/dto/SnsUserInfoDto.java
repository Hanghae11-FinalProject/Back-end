package com.team11.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnsUserInfoDto {

    private Long id;

    private String nickname;

    private String email;

    private String profile;
}
