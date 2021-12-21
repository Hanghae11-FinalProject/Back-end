package com.team11.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team11.backend.model.CurrentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
                @JsonProperty("currentState") String currentState
        ){
            this.title = title;
            this.content = content;
            this.category = category;
            this.currentState = CurrentState.valueOf(currentState);
        }
    }
}
