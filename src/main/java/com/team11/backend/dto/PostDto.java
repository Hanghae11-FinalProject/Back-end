package com.team11.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team11.backend.model.*;
import lombok.*;

import java.util.List;

public class PostDto {

    @Setter
    @Getter
    public static class RequestDto{
        private String title;
        private String content;
        private String category;
        private String myItem;
        private String exchangeItem;
        private CurrentState currentState;
        private List<TagDto.RequestDto> tagRequestDtos;

        @JsonCreator
        public RequestDto(
                @JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("category") String category,
                @JsonProperty("currentState") String currentState,
                @JsonProperty("myItem") String myItem,
                @JsonProperty("exchangeItem") String exchangeItem,
                @JsonProperty("tag") List<TagDto.RequestDto> tagReqeustDtos
        ){
            this.title = title;
            this.content = content;
            this.category = category;
            this.currentState = CurrentState.valueOf(currentState);
            this.tagRequestDtos = tagReqeustDtos;
            this.myItem = myItem;
            this.exchangeItem =exchangeItem;
        }
    }

    @Setter
    @Getter
    public static class PutRequestDto{
        private String title;
        private String content;
        private String category;
        private String myItem;
        private String exchangeItem;
        private CurrentState currentState;
        private List<TagDto.RequestDto> tagRequsetDtos;
        private List<ImageUrlDto> imageUrlDtos;

        @JsonCreator
        public PutRequestDto(
                @JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("category") String category,
                @JsonProperty("currentState") String currentState,
                @JsonProperty("myItem") String myItem,
                @JsonProperty("images") List<ImageUrlDto> imageUrlDtos,
                @JsonProperty("exchangeItem") String exchangeItem,
                @JsonProperty("tag") List<TagDto.RequestDto> tagReqeustDtos
        ){
            this.title = title;
            this.content = content;
            this.category = category;
            this.currentState = CurrentState.valueOf(currentState);
            this.tagRequsetDtos = tagReqeustDtos;
            this.imageUrlDtos =imageUrlDtos;
            this.myItem = myItem;
            this.exchangeItem =exchangeItem;
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
        private String myItem;
        private String exchangeItem;
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
        private String createdAt;
        private Long postId;
        private String nickname;
        private String profileImg;
        private String title;
        private String content;
        private String address;
        private String myItem;
        private String exchangeItem;
        private String categoryName;
        private Integer commentCount;
        private Integer bookMarkCount;
        private CurrentState currentState;
        private List<Tag> tags;
        private List<Image> images;
        private List<BookMarkDto.DetailResponseDto> bookMarks;
        private List<CommentDto.ResponseDto> comments;
    }

    @Getter
    @Setter
    @Builder
    public static class ShowPostRoomDto{
        private String myItem;
        private String exchangeItem;
    }

}
