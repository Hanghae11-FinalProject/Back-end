package com.team11.backend.security.oauth2.user;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getName() {
        Map profiles = (Map) attributes.get("profile");
        return (String)profiles.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }


}
