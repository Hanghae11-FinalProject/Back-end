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
    // userId 보내야됨
    @Getter
    @Setter
    public static class findRoomDto{
        private String roomName;
        private Long postId;
        private Long userId;
        private Long toUserId;
    }

    // userId 보내야됨
    @Getter
    @Setter
    public static class UpdateCountDto{
        private String roomName;
        private Long userId;
    }

    @Getter
    @Setter
    @Builder
    public static class Response{
        private ChatUserDto user;
        private String roomName;
    }
}
