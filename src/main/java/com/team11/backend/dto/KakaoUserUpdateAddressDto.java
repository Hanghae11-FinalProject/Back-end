package com.team11.backend.dto;

import com.team11.backend.model.User;
import lombok.*;

public class KakaoUserUpdateAddressDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDto {
        private String address;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto {
        private Long userId;
        private String username;
        private String nickName;
        private String profileImg;
        private String address;
    }

    public static KakaoUserUpdateAddressDto.ResponseDto convertDto(User user){
       return KakaoUserUpdateAddressDto.ResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickName(user.getNickname())
                .profileImg(user.getProfileImg())
                .address(user.getAddress())
                .build();
    }
}
