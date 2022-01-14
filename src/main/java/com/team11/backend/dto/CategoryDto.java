package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import lombok.*;
import java.util.List;

public class CategoryDto {


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {
        private List<String> categoryName;
        private List<String> address;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private String categoryName;
        private Long postId;
        private String profileImg;
        private String username;
        private String nickname;
        private String title;
        private String content;
        private String address;
        private List<Image> images;
        private List<BookMarkDto.DetailResponseDto> bookMarks;
        private CurrentState currentState;
        private String myItem;
        private String exchangeItem;
        private String createdAt;
        private int bookmarkCnt;
        private int commentCnt;

    }




}