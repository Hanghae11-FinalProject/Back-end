package com.team11.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.dto.HeaderDto;
import com.team11.backend.dto.SnsUserInfoDto;
import com.team11.backend.jwt.JwtTokenProvider;
import com.team11.backend.model.AuthProvider;
import com.team11.backend.model.User;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public HeaderDto kakaoLogin(
            String code
    ) throws JsonProcessingException {
// 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

// 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        SnsUserInfoDto snsUserInfoDto = getKakaoUserInfo(accessToken);

// 3. "카카오 사용자 정보"로 필요시 회원가입  및 이미 같은 이메일이 있으면 기존회원으로 로그인
        User kakaoUser = registerKakaoOrUpdateKakao(snsUserInfoDto);

// 4. 강제 로그인 처리

        return forceLogin(kakaoUser);
    }

    private String getAccessToken(
            String code
    ) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "397c0abcfdf1e8bb72359a99d7784e74");
        body.add("redirect_uri", "http://localhost:3000/oauth/callback/kakao");
        // body.add("redirect_uri","http://localhost:3000/oauth/callback/kakao");
        // https://kauth.kakao.com/oauth/authorize?client_id=dcd2dc8ef9a91776b876f76145451b0f&redirect_uri=http://52.78.31.61:3000/oauth/kakao/callback&response_type=code
        body.add("code", code);



// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SnsUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email;
        if(jsonNode.get("kakao_account").get("email") == null)
            email = UUID.randomUUID().toString() + "@contap.com";
        else
            email = jsonNode.get("kakao_account")
                    .get("email").asText();

        String profileStr = "http://www.city.kr/files/attach/images/161/701/416/022/a2c34aa75756074e20552ccbac6894e8.jpg";

        return new SnsUserInfoDto(id, nickname, email,profileStr);
    }

    private User registerKakaoOrUpdateKakao(
            SnsUserInfoDto snsUserInfoDto
    ) {
        User sameUser = userRepository.findByUsername(snsUserInfoDto.getEmail()).orElse(null);

        if (sameUser == null) {
            return registerKakaoUserIfNeeded(snsUserInfoDto);
        } else {
            return updateKakaoUser(sameUser);
        }
    }

    private User registerKakaoUserIfNeeded(
            SnsUserInfoDto snsUserInfoDto
    ) {
// DB 에 중복된 Kakao Id 가 있는지 확인
        String username = snsUserInfoDto.getEmail();
        User kakaoUser = userRepository.findByUsername(username)
                .orElse(null);
        if (kakaoUser == null) {
// 회원가입
// username: kakao nickname
            String nickname = snsUserInfoDto.getNickname();

// password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

// email: kakao email
            String email = snsUserInfoDto.getEmail();
            kakaoUser = User.builder()
                    .username(email)
                    .provider(AuthProvider.kakao)
                    .password(encodedPassword)
                    .nickname(nickname)
                    .profileImg("http://www.city.kr/files/attach/images/161/701/416/022/a2c34aa75756074e20552ccbac6894e8.jpg")
                    .build();
            userRepository.save(kakaoUser);

        }
        return kakaoUser;
    }

    private User updateKakaoUser(
            User sameUser
    ) {
        if (sameUser.getUsername() == null) {
            System.out.println("중복");
            sameUser.setUsername(sameUser.getUsername());
            sameUser.setNickname(sameUser.getNickname());
            userRepository.save(sameUser);
        }
        return sameUser;
    }

    private HeaderDto forceLogin(
            User kakaoUser
    ) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HeaderDto headerDto = new HeaderDto();
        headerDto.setTOKEN(jwtTokenProvider.createToken(kakaoUser.getNickname(),Long.toString(kakaoUser.getId())));
        headerDto.setUserId(kakaoUser.getId());
        headerDto.setNickName(kakaoUser.getNickname());
        headerDto.setProfileImg(kakaoUser.getProfileImg());
        return headerDto;
    }
}