package com.team11.backend.dto;

import com.team11.backend.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostDto {

    @Getter
    @Builder
    public static class RequestDto{

    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{
        private Long postId;
        private String nickname;
        private String title;
        private String content;
        private String address;
        private List<Image> images;
        private CurrentState currentState;
        private String createdAt;
    }
}
