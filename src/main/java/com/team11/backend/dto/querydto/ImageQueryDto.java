package com.team11.backend.dto.querydto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ImageQueryDto  {
    private Long postId;
    private Long imageId;
    private String imageName;
    private String imageUrl;


    @QueryProjection
    public ImageQueryDto(Long postId, Long imageId, String imageName, String imageUrl) {
        this.postId = postId;
        this.imageId = imageId;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}
