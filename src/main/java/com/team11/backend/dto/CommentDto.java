package com.team11.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CommentDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {

        @NotBlank(message = "댓글을 입력해주세요.")
        private String content;

        @NotNull(message = "게시글 아이디를 입력해주세요.")
        @Positive(message = "올바른 게시글 아이디를 입력해주세요.")
        private Long postId;

        private Long parentId;
    }

    @Data
    @NoArgsConstructor
    public static class ResponseDto {

        private Long id;
        private String content;
        private Long userId;
        private String nickname;
        private String profileImg;
        private String createAt;
        private final List<CommentDto.ResponseDto> children = new ArrayList<>();

        @Builder

        public ResponseDto(Long id, String content, Long userId, String nickname, String profileImg, String createAt) {
            this.id = id;
            this.content = content;
            this.userId = userId;
            this.nickname = nickname;
            this.profileImg = profileImg;
            this.createAt = createAt;
        }
    }
}
