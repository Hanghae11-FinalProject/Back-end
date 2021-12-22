package com.team11.backend.security.oauth2;

import com.team11.backend.config.ApplicationProperties;
import com.team11.backend.exception.authexception.OAuth2AuthenticationEx;
import com.team11.backend.security.jwtutil.JwtUtil;
import com.team11.backend.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.team11.backend.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final JwtUtil jwtUtil;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, ApplicationProperties applicationProperties) {
        this.jwtUtil = jwtUtil;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response,authentication);
        log.info("리다이렉트 타겟={}",targetUrl);

        if(response.isCommitted()){
            log.debug("응답 완료되어 리다이렉트 할수 없는 Url" + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
    /**
     * SuccessHandler의 여러가지 메서드중 로그인 성공시 이동경로를 설정
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);


        if(!isAuthorizedRedirectUri(redirectUri.get())){
            throw new OAuth2AuthenticationEx("리다이렉트 URI 가없으면 인증 진행이 불가능합니다.", HttpStatus.BAD_REQUEST);
        }


        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = jwtUtil.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        HttpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestResponse(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        //host와 port가 맞는지 다른지 검증 사용자들이 다른 경로를 사용할 수도 있음
        return applicationProperties.getoAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
