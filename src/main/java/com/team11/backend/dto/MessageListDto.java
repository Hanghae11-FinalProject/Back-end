package com.team11.backend.dto;

import com.team11.backend.dto.chat.MessageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MessageListDto {
    private PostDto.ShowPostRoomDto post;
    private List<MessageDto> message;
}
