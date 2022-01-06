package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatUserDto {
    private String nickname;
    private String profileImg;
    private Long userId;
}
