package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CategoryDto {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {
        private List<String> categoryName;
        private String address;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private String categoryName;
        private Long postId;
        private String username;
        private String nickname;
        private String title;
        private String content;
        private String address;
        private List<Image> images;
        private CurrentState currentState;
        private String createdAt;

    }




}