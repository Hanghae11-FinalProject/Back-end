package com.team11.backend.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.dto.SignupDto;
import com.team11.backend.security.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());

        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

        // UserId 내려주기
        response.setContentType("application/json");
        SignupDto.ResponseDto userId = SignupDto.ResponseDto.builder()
                .userId(userDetails.getUser().getId())
                .profileImg(userDetails.getUser().getProfileImg())
                .nickName(userDetails.getUser().getNickname())
                .build();

        String result = mapper.writeValueAsString(userId);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(result);
    }
}
