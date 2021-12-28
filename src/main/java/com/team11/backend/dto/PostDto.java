package com.team11.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import com.team11.backend.model.Tag;
import lombok.*;

import java.util.List;

public class PostDto {

    @Setter
    @Getter
    public static class RequestDto{
        private String title;
        private String content;
        private String category;
        private CurrentState currentState;
        private List<TagDto.RequestDto> tagRequsetDtos;

        @JsonCreator
        public RequestDto(
                @JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("category") String category,
                @JsonProperty("currentState") String currentState,
                @JsonProperty("tag") List<TagDto.RequestDto> tagReqeustDtos
        ){
            this.title = title;
            this.content = content;
            this.category = category;
            this.currentState = CurrentState.valueOf(currentState);
            this.tagRequsetDtos = tagReqeustDtos;
        }
    }
  
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{
        private Long postId;
        private String nickname;
        private List<BookMarkDto.DetailResponseDto> bookMarks;
        private String title;
        private String content;
        private String address;
        private List<Tag> tags;
        private List<Image> images;
        private CurrentState currentState;
        private String createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailResponseDto{
        private Long postId;
        private String nickname;
        private String profileImg;
        private List<BookMarkDto.DetailResponseDto> bookMarks;
        private String title;
        private String content;
        private String address;
        private List<Tag> tags;
        private List<Image> images;
        private CurrentState currentState;
        private String createdAt;
    }
}
