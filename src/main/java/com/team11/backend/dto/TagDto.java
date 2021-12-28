package com.team11.backend.dto;

import lombok.*;

public class TagDto {

    @Setter
    @Getter
    public static class RequestDto{
        private String tagName;
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Setter
    @Getter
    public static class ResponseDto{
        private String tagName;
    }
}
