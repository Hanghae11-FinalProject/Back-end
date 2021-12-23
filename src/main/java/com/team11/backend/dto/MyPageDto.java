package com.team11.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MyPageDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestDto{
        private String nickname;
        private String profileImg;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto{
        private String nickname;
        private String profileImg;
    }

}
