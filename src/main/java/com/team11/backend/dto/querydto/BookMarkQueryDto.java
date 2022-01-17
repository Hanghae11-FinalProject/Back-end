package com.team11.backend.dto.querydto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookMarkQueryDto {
    private Long postId;
    private Long userId;

    @QueryProjection
    public BookMarkQueryDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
