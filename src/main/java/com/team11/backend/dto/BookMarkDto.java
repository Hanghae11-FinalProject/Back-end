package com.team11.backend.dto;

import com.team11.backend.model.BookMark;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ResponseDto> convertToBookMarkDto(List<BookMark> bookMarkList) {
        return bookMarkList.stream().map(s -> {

                    if (s.getPost().getImages().size() == 0)
                        return new BookMarkDto.ResponseDto(
                                s.getPost().getId(),
                                s.getPost().getUser().getId(),
                                s.getPost().getTitle(),
                                null,
                                s.getUser().getAddress(),
                                TimeConversion.timeConversion(s.getPost().getCreatedAt()),
                                s.getPost().getCurrentState().name());
                    else
                        return new BookMarkDto.ResponseDto(
                                s.getPost().getId(),
                                s.getPost().getUser().getId(),
                                s.getPost().getTitle(),
                                s.getPost().getImages().get(0).getImageUrl(),
                                s.getUser().getAddress(),
                                TimeConversion.timeConversion(s.getPost().getCreatedAt()),
                                s.getPost().getCurrentState().name());
                }
        ).collect(Collectors.toList());
    }

}
