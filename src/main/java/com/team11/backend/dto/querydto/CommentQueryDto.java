package com.team11.backend.dto.querydto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Getter
public class CommentQueryDto {
    private Long postId;
    private Long commentId;
    private String content;
    private Long userId;
    private String nickname;
    private String profileImg;
    private String createdAt;
    private final List<CommentQueryDto> children = new ArrayList<>();

    @Builder
    @QueryProjection
    public CommentQueryDto(Long postId, Long commentId, String content, Long userId, String nickname, String profileImg, String createdAt) {
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.createdAt = createdAt;
    }

    public CommentQueryDto(Long commentId, String content, Long userId, String nickname, String profileImg, String createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.createdAt = createdAt;
    }
}
