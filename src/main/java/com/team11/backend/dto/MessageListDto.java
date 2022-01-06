package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageListDto {
    private ChatUserDto user;
    private MessageContentDto message;
}
