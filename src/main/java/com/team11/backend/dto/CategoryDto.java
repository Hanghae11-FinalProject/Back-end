package com.team11.backend.dto;

import com.team11.backend.model.Post;
import lombok.*;

import java.util.List;

public class CategoryDto {

    @Setter
    @Getter
    public static class RequestDto {
        private String categoryName;
        private String region;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private Long Id;
        private String categoryName;
        private List<Post> posts;

        public void CategoryResponseDto(Long Id, String categoryName) {
            this.Id = Id;
            this.categoryName = categoryName;
        }
    }


}
