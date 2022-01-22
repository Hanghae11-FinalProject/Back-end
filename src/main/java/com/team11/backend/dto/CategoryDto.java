package com.team11.backend.dto;

import com.team11.backend.model.BookMark;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import com.team11.backend.model.Post;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {


    @Data
    @Builder
    @ToString
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

    public static CategoryDto.ResponseDto categoryResponseDto(Post post , BookMarkRepository bookMarkRepository , CommentRepository commentRepository) {
        return CategoryDto.ResponseDto.builder()
                .categoryName(post.getCategory())
                .postId(post.getId())
                .profileImg(post.getUser().getProfileImg())
                .username(post.getUser().getUsername())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .address(post.getUser().getAddress())
                .images(post.getImages())
                .currentState(post.getCurrentState())
                .myItem(post.getMyItem())
                .exchangeItem(post.getExchangeItem())
                .createdAt(TimeConversion.timeConversion(post.getCreatedAt()))
                .bookmarkCnt(bookMarkRepository.countByPost(post).orElse(0))
                .commentCnt(commentRepository.countByPost(post).orElse(0))
                .bookMarks(post.getBookMarks().stream()
                        .map(CategoryDto::toBookmarkResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
    }



}