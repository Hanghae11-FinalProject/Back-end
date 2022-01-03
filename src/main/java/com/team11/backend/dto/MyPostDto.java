package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import lombok.*;

public class MyPostDto {

    @Getter
    @Setter
    @Builder
    public static class ResponseDto{
        private Long postId;
        private String title;
        private String content;
        private String createdAt;
        private String address;
        private CurrentState currentState;
        private int bookmarkCnt;
        private int commentCnt;
    }
}
