package com.team11.backend.dto;

import com.team11.backend.model.User;
import com.team11.backend.security.UserDetailsImpl;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeaderDto {
    private String TOKEN;
    private Long userId;
    private String nickName;
    private String profileImg;
    private String address;


    public static HeaderDto createHeaderDto(String token, User kakaoUser){
        return HeaderDto.builder()
                .TOKEN(token)
                .userId(kakaoUser.getId())
                .nickName(kakaoUser.getNickname())
                .profileImg(kakaoUser.getProfileImg())
                .address(kakaoUser.getAddress())
                .build();
    }
}
