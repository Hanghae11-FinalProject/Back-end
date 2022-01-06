package com.team11.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

    @Getter
    @Setter
    public static class Reqeust{
        private Long postId;
        private Long toUserId;
    }

    @Getter
    @Setter
    @Builder
    public static class Response{
        private ChatUserDto user;
        private String roomName;
    }
}
