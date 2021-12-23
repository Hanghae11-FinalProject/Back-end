package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import com.team11.backend.model.Post;
import lombok.*;

import java.util.List;

public class CategoryDto {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {
        private String categoryName;
        private String region;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private String categoryName;
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