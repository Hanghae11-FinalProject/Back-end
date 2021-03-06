package com.team11.backend.controller;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(S3MockConfig.class)
@Rollback
class UserControllerTest {

    @Autowired
    S3Mock s3Mock;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String token = "";

    private final UserDto user = UserDto.builder()
            .username("xxx@naver.com")
            .password("1234aa@@")
            .passwordCheck("1234aa@@")
            .nickname("diddl")
            .address("서울특별시 강남구")
            .build();

    private final LoginDto login = LoginDto.builder()
            .username("xxx@naver.com")
            .password("1234aa@@")
            .build();

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(1)
    @DisplayName("회원 가입")
    void test1() throws JsonProcessingException {
        // given
        String requestBody = objectMapper.writeValueAsString(user);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<Object> response = restTemplate.postForEntity(
                "/user/signup",
                request,
                Object.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody());
    }

    @Test
    @Order(2)
    @DisplayName("로그인, JWT 토큰 받기")
    void test2() throws JsonProcessingException {
        // given
        String requestBody = objectMapper.writeValueAsString(login);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<Object> response = restTemplate.postForEntity(
                "/user/login",
                request,
                Object.class);

        // then
        token = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Nested
    @DisplayName("JWT 토큰 인가 - 유저 정보 수정")
    class JWTtest {
        @Test
        @DisplayName("유저 정보 수정")
        void test2() throws JsonProcessingException {
            // given
            UserDto userUpdate = UserDto.builder()
                    .username("woojin126@naver.com")
                    .nickname("woojins")
                    .address("서울특별시")
                    .password("1234aa@@")
                    .passwordCheck("1234aa@@")
                    .build();

            String requestBody = objectMapper.writeValueAsString(userUpdate);
            headers.set("Authorization", token);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // when
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    "/api/userInfos",
                    request,
                    Object.class);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class UserDto {
        private String username;
        private String password;
        private String passwordCheck;
        private String nickname;
        private String address;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class LoginDto {
        private String username;
        private String password;
    }

    @AfterAll
    public void shutdownMockS3(){
        s3Mock.stop();
    }
}*/
