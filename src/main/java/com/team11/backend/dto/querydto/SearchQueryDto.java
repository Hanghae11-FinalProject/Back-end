package com.team11.backend.dto.querydto;

import com.querydsl.core.annotations.QueryProjection;
import com.team11.backend.model.CurrentState;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchQueryDto {

    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private String address;
    private String myItem;
    private String exchangeItem;
    private String profileImg;
    private CurrentState currentState;
    private String createdAt;
    private Integer bookmarkCnt;
    private Integer commentCnt;
    private List<ImageQueryDto> images;
    private List<BookMarkQueryDto> bookMarks;

    @QueryProjection
    public SearchQueryDto(Long postId, String nickname, String title, String content, String address, String myItem, String exchangeItem, String profileImg, CurrentState currentState, LocalDateTime createdAt, Integer bookmarkCnt, Integer commentCnt) {
        this.postId = postId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.address = address;
        this.myItem = myItem;
        this.exchangeItem = exchangeItem;
        this.profileImg = profileImg;
        this.currentState = currentState;
        this.createdAt = TimeConversion.timeConversion(createdAt);
        this.bookmarkCnt = bookmarkCnt;
        this.commentCnt = commentCnt;
    }
}
