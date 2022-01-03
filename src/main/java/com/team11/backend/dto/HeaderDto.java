package com.team11.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderDto {
    private String TOKEN;
    private Long userId;
    private String nickName;
    private String profileImg;
}
