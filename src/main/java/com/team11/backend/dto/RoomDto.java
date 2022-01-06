package com.team11.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {
    private Long postId;
    private Long toUserId;

    public Long getPostId() {
        return postId;
    }
}
