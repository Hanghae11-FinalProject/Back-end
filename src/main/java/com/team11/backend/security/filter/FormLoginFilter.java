package com.team11.backend.security.filter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {
    final private ObjectMapper objectMapper;

    public FormLoginFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            // ** 1. 클라이언트 JSON -> 자바 객체 형태로 변경 **
            JsonNode requestBody = objectMapper.readTree(request.getInputStream());
            String username = requestBody.get("username").asText();
            String password = requestBody.get("password").asText();

            // ** 2. username, password 요청 변수 생성 **
            authRequest = new UsernamePasswordAuthenticationToken(username, password);
        } catch (Exception e) {
            throw new RuntimeException("username, password 입력이 필요합니다. (JSON)");
        }

        // ** 3. 인증 요청 **
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}