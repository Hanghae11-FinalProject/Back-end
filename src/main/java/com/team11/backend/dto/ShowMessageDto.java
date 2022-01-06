package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;

import java.util.List;


public class ShowMessageDto {

    @Getter
    @Setter
    @Builder
    public static class RequestDto{
        private String roomName;
    }

    @Getter
    @Setter
    @Builder
    public static class ResponseDto {
        private PostDto.ShowPostRoomDto post;
        private List<MessageListDto> messages;
    }
}
