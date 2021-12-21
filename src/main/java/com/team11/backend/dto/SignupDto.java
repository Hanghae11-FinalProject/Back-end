package com.team11.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignupDto {

    @Getter
    @Builder
    public static class RequestDto{
        private String email;
        private String nickname;
        private String password;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{
        private Long userId;
    }
}