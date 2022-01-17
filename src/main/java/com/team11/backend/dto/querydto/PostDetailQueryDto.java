package com.team11.backend.dto.querydto;

import com.querydsl.core.annotations.QueryProjection;
import com.team11.backend.model.CurrentState;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class PostDetailQueryDto {
    private Long postId;
    private String username;
    private String nickname;
    private String address;
    private String title;
    private String profileImg;
    private String content;
    private String myItem;
    private String exchangeItem;
    private CurrentState currentState;
    private String categoryName;
    private String createAt;
    private Long bookmarkCnt;
    private Long commentCnt;
    private List<TagQueryDto> tags;
    private List<ImageQueryDto> images;
    private List<BookMarkQueryDto> bookMarks;
    private List<CommentQueryDto> comments;

    @QueryProjection
    public PostDetailQueryDto(Long postId, String username, String nickname, String address, String title, String profileImg, String content, String myItem, String exchangeItem, CurrentState currentState, String categoryName, String createAt, Long bookmarkCnt, Long commentCnt) {
        this.postId = postId;
        this.username = username;
        this.nickname = nickname;
        this.address = address;
        this.title = title;
        this.profileImg = profileImg;
        this.content = content;
        this.myItem = myItem;
        this.exchangeItem = exchangeItem;
        this.currentState = currentState;
        this.categoryName = categoryName;
        this.createAt = createAt;
        this.bookmarkCnt = bookmarkCnt;
        this.commentCnt = commentCnt;
    }
}
