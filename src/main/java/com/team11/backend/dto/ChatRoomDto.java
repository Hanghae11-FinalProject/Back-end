package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomDto {
    private String roomName;
    private Long postId;
    private ChatUserDto user;
    private LastMessageDto lastMessage;
    private CurrentState currentState;
    private int notReadingMessageCount;
}
