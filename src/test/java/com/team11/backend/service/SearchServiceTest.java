package com.team11.backend.service;

import com.team11.backend.model.*;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.TagRepository;
import com.team11.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback
class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagRepository tagRepository;

    Post post;
    User user;


    @BeforeEach
    public void setup() {
        user = User.builder()
                .address("서울특별시 강북구")
                .password("1234aabb@@")
                .nickname("woojin")
                .username("woojin126@naver.com")
                .profileImg("hi.jpg")
                .provider(AuthProvider.kakao)
                .build();
        userRepository.save(user);

        Tag mando = Tag.builder()
                .tagName("냉동만두")
                .build();
        tagRepository.save(mando);
        Tag lamen = Tag.builder()
                .tagName("냉동만두")
                .build();
        tagRepository.save(lamen);

        List<Tag> tagList = new ArrayList<>();
        tagList.add(mando);
        tagList.add(lamen);

        post = Post.builder()
                .myItem("아이템")
                .exchangeItem("아이템")
                .currentState(CurrentState.Proceeding)
                .title("냉동만두")
                .content("냉동만두랑 비비고랑 바꾸실분")
                .user(user)
                .category("food")
                .tags(tagList)
                .build();

        postRepository.save(post);
    }
    

    @Nested
    @DisplayName("검색조건")
    class search{

        @Nested
        @DisplayName("성공 케이스")
        class success{

            @Test
            @DisplayName("태그로 검색")
            void test_1(){
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            }
        }


        @Nested
        @DisplayName("실패 케이스")
        class fail{
            
        }
    }
}