package com.team11.backend.dto.querydto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class TagQueryDto {
    private Long postId;
    private Long tagId;
    private String tagName;


    @QueryProjection
    public TagQueryDto(Long postId, Long tagId, String tagName) {
        this.postId = postId;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
