package com.team11.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnsUserInfoDto {

    private Long id;

    private String nickName;

    private String email;

    private String profileImg;
}
