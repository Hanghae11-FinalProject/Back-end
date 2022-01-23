package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MyPostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private String createdAt;
    private String address;
    private CurrentState currentState;
    private int bookmarkCnt;
    private int commentCnt;

    @Builder
    public MyPostResponseDto(Long postId, String title, String content, LocalDateTime createdAt, String address, CurrentState currentState, int bookmarkCnt, int commentCnt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = TimeConversion.timeConversion(createdAt);
        this.address = address;
        this.currentState = currentState;
        this.bookmarkCnt = bookmarkCnt;
        this.commentCnt = commentCnt;
    }
}
