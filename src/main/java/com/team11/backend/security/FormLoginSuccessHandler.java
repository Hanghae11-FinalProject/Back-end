package com.team11.backend.security;

import com.team11.backend.security.jwtutil.JwtUtil;
import com.team11.backend.security.oauth2.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String NICKNAME = "Nickname";
    public static final String IMAGEURL = "profileImage";
    // ** 5. FormLogin 성공 및 JWT 토큰 생성 **
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final CustomUserDetails userDetails = ((CustomUserDetails) authentication.getPrincipal());
        byte[] bytes = userDetails.getUser().getNickname().getBytes(StandardCharsets.UTF_8);
        byte[] bytes1 = userDetails.getUser().getProfileImg().getBytes(StandardCharsets.UTF_8);
        // Token 생성
        String token = jwtUtil.createToken(authentication);
        String encodedNickname = Base64.getEncoder().encodeToString(bytes);
        String encodedImageUrl = Base64.getEncoder().encodeToString(bytes1);
        response.addHeader(NICKNAME, encodedNickname);
        response.addHeader(IMAGEURL, encodedImageUrl);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
    }

}