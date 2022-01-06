package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomDto {
    private String roomName;
    private ChatUserDto user;
    private LastMessageDto lastMessage;
    private int notReadingMessageCount;
}
