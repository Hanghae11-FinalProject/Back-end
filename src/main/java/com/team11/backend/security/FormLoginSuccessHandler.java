package com.team11.backend.security;

import com.team11.backend.security.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";

    // ** 5. FormLogin 성공 및 JWT 토큰 생성 **
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        // Token 생성
        String token = jwtUtil.createToken(authentication);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
    }

}