package com.team11.backend.dto;

import com.team11.backend.model.*;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.*;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class SearchDto {


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {
        private List<String> keyword;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
//        private String keyword;
        private Long postId;
        private String nickname;
        private String title;
        private String content;
        private String address;
        private String myItem;
        private String exchangeItem;
        private String profileImg;
        private List<Image> images;
        private List<BookMarkDto.DetailResponseDto> bookMarks;
        private CurrentState currentState;
        private String createdAt;
        private int bookmarkCnt;
        private int commentCnt;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TotalResponseDto {
        private Long postCnt;
        private List<SearchDto.ResponseDto> posts;

    }

    public static List<SearchDto.ResponseDto> searchResponseDto(PageImpl<Post> posts , BookMarkRepository bookMarkRepository , CommentRepository commentRepository) {
        return posts.stream()
                .map(s -> new SearchDto.ResponseDto(
                        s.getId(),
                        s.getUser().getNickname(),
                        s.getTitle(),
                        s.getContent(),
                        s.getUser().getAddress(),
                        s.getMyItem(),
                        s.getExchangeItem(),
                        s.getUser().getProfileImg(),
                        s.getImages(),
                        s.getBookMarks().stream()
                                .map(SearchDto::toBookmarkResponseDto)
                                .collect(Collectors.toList()),
                        s.getCurrentState(),
                        TimeConversion.timeConversion(s.getCreatedAt()),
                        bookMarkRepository.countByPost(s).orElse(0),
                        commentRepository.countByPost(s).orElse(0)))
                .collect(Collectors.toList());
    }

    public static BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
    }
}
