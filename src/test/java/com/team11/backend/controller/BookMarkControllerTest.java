package com.team11.backend.controller;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.config.S3MockConfig;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.dto.UserDto;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Post;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.service.PostService;
import io.findify.s3mock.S3Mock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookMarkControllerTest {

    @Autowired
    S3Mock s3Mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

@Autowired
    private WebApplicationContext context; // MockMvc ?????? ????????? ?????? context;

    private MockMvc mockMvc; // controller??? request??? ??????????????? mock ??????

    @MockBean
    private PostRepository postRepository;


    ObjectMapper mapper; // ????????? json ???????????? ?????? ??? ??????

    @Autowired
    BookMarkController bookMarkController;

    @BeforeEach // @Test ????????? ??????
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookMarkController).build();
        mapper = new ObjectMapper();
    }


    @Test
    @WithMockUser
    public void bookMarkAddTest() throws Exception {
        //given
        given(postRepository.save(any()))
                .willReturn(Post.builder()
                        .id(1L)
                        .myItem("??????")
                        .exchangeItem("??????")
                        .currentState(CurrentState.Proceeding)
                        .title("?????????????????????")
                        .content("???????????????")
                        .category("??????")
                        .images(new ArrayList<>())
                        .tags(new ArrayList<>())
                        .build());
        when(postRepository.save(any()))
                .thenReturn(Post.builder()
                        .id(1L)
                        .myItem("??????")
                        .exchangeItem("??????")
                        .currentState(CurrentState.Proceeding)
                        .title("?????????????????????")
                        .content("???????????????")
                        .category("??????")
                        .images(new ArrayList<>())
                        .tags(new ArrayList<>())
                        .build());

        mockMvc.perform(post("/api/bookmark/1L")
                .contentType(MediaType.APPLICATION_JSON))// contentType??? json ??????
                .andExpect(status().isOk())     // ???????????? OK??? ????????? ????????????
                .andDo(print());// ?????? ????????? ??????

    }


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    BookMarkRepository bookMarkRepository;
    private HttpHeaders headers;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String token = "";

    private Long postId;
    private final UserDto user = UserDto.builder()
            .username("xxx@naver.com")
            .password("1234aa@@")
            .passwordCheck("1234aa@@")
            .nickname("diddl")
            .address("??????????????? ?????????")
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
    @DisplayName("?????? ??????")
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
    @DisplayName("?????????, JWT ?????? ??????")
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

    @AfterEach
    public void shutdownMockS3(){
        s3Mock.stop();
    }
} */