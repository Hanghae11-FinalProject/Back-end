package com.team11.backend.security.oauth2.user;


import com.team11.backend.exception.authexception.OAuth2AuthenticationEx;
import com.team11.backend.model.AuthProvider;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String,Object> attributes){

        if(registrationId.equalsIgnoreCase(AuthProvider.google.name())){
            return new GoogleOAuth2UserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(AuthProvider.naver.toString())) {
            return new NaverOAuth2UserInfo((Map<String, Object>) attributes.get("response"));
        }else if(registrationId.equalsIgnoreCase(AuthProvider.kakao.name())){
            Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
            return new KakaoOAuth2UserInfo(kakao_account);
        }else{
            throw new OAuth2AuthenticationEx("죄송합니다 아직 지원하지않는 소셜입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
