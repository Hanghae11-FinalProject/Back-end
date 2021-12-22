package com.team11.backend.security.oauth2;

import com.team11.backend.security.oauth2.service.CustomUserDetails;
import com.team11.backend.security.oauth2.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@RequiredArgsConstructor
public class FormLoginAuthProvider implements AuthenticationProvider {

//    @Resource(name="customUserDetailsService")
//    private UserDetailsService userDetailsService;
    private CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        // FormLoginFilter 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회
        String username = token.getName();
        String password = (String) token.getCredentials();

        // UserDetailsService 를 통해 DB에서 username 으로 사용자 조회
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        System.out.println(password +"aa");
        System.out.println(userDetails.getPassword() +"bb");
        System.out.println(userDetails.getUsername()+"cc");
        System.out.println(authentication.getName() + "dd");

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(userDetails.getUsername() + "Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}