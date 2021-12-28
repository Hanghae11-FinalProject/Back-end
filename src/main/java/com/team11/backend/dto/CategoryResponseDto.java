package com.team11.backend.dto;

import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {

    private String categoryName;
    private Long postId;
    private String username;
    private String nickname;
    private String title;
    private String content;
    private String address;
    private String myItem;
    private String exchangeItem;
    private List<Image> images;
    private CurrentState currentState;
    private String createdAt;
}
