package com.team11.backend.config;

import com.team11.backend.exception.authexception.Http401ErrorEntryPoint;
import com.team11.backend.security.FormLoginSuccessHandler;
import com.team11.backend.security.filter.FormLoginFilter;
import com.team11.backend.security.filter.JwtAuthFilter;
import com.team11.backend.security.jwtutil.JwtUtil;
import com.team11.backend.security.oauth2.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthFilter jwtAuthFilter;
    private JwtUtil jwtUtil;
    private CustomOAuth2UserService customOauth2Userervice;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    @Lazy
    public SecurityConfig(CustomOAuth2UserService customOauth2Userervice,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          JwtAuthFilter jwtAuthFilter,
                          JwtUtil jwtUtil
                         ){
        this.customOauth2Userervice = customOauth2Userervice;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // CustomAuthenticationProvider()를 호출하기 위해서 Overriding
        auth
                .authenticationProvider(formLoginAuthProvider());
    }

    public void configure(HttpSecurity http) throws Exception {

        http.
                cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션정책을 설정한다는것은 인증 처리 관점에서 스프링 시큐리티가 더 이상 세션쿠키 방식의 인증 메카니즘으로 인증처리를 하지 않겠다는 의미
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new Http401ErrorEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/user/signup",
                        "/user/login",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .authorizationEndpoint()//인가에 대한 요청을 서비스할 때 사용한다. 기본 URL은 /oauth/authorize
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository()) // 인가 요청을 시작한 시점부터 인가 요청을 받는 시점까지 (콜백) OAuth2AuthorizationRequest를 유지해준다.
                .and()
                .redirectionEndpoint()//인가 서버가 리소스 소유자의 user-agent를 통해 가져온 인가 응답을 클라이언트에게 전송할 때 사용
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOauth2Userervice)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/user/login");
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    private AuthenticationSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler(jwtUtil);
    }

    @Bean
    public Http401ErrorEntryPoint http401ErrorEntryPoint() {
        return new Http401ErrorEntryPoint();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}

