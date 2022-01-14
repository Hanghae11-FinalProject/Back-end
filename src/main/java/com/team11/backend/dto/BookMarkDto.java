package com.team11.backend.dto;

import lombok.*;

public class BookMarkDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private Long postId;
        private Long postUserId;
        private String title;
        private String image;
        private String address;
        private String createdAt;
        private String currentState;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailResponseDto {
        private Long userId;
    }
}
