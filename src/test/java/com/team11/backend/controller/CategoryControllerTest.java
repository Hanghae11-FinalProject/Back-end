package com.team11.backend.controller;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.dto.CategoryResponseDto;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.repository.querydsl.CategoryRepository;
import com.team11.backend.service.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Transactional
@Rollback
class CategoryControllerTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    User user;
    User user1;
    Post post;
    Post post1;
    Post post2;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .username("woojin126@naver.com")
                .nickname("woojin")
                .password("1234aabb@@")
                .address("서울특별시 강북구")
                .build();

        userRepository.save(user);

        user1 = User.builder()
                .username("woojin1267@naver.com")
                .nickname("woojinn")
                .password("1234aabb@@")
                .address("서울특별시 강서구")
                .build();

        userRepository.save(user1);

        post = Post.builder()
                .category("cloth")
                .user(user)
                .content("나이키?")
                .title("나이키")
                .currentState(CurrentState.Proceeding)
                .build();

        postRepository.save(post);

        post1 = Post.builder()
                .category("food")
                .user(user)
                .content("수박")
                .title("수박")
                .currentState(CurrentState.Proceeding)
                .build();

        postRepository.save(post1);


        post2 = Post.builder()
                .category("food")
                .user(user1)
                .content("수박")
                .title("수박")
                .currentState(CurrentState.Proceeding)
                .build();

        postRepository.save(post2);

    }

    @Nested
    @DisplayName("카테고리 필터링 기준 -> 지역 : 시 or 구 or (음식,가전제품,의류 등 중하나")
    class categoryTest {

        @Nested
        @DisplayName("성공케이스")
        class Success {
            @Test
            @DisplayName("검색조건1 : (cloth)  and (검색조건2 : 서울특별시)")
            void test1() {
                //given
                List<String> categoryName = new ArrayList<>();
                categoryName.add("cloth");


                List<String> city = new ArrayList<>();
                city.add("서울특별시");


                CategoryDto.RequestDto categoryFilter = CategoryDto.RequestDto.builder()
                        .categoryName(categoryName)
                        .address(city)
                        .build();


                //when
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                List<CategoryResponseDto> categoryResponseDtos = categoryService.categoryFilter(categoryFilter, pageRequest);
                CategoryResponseDto categoryResponseDto = categoryResponseDtos.get(0);

                //then
                Assertions.assertEquals(1, categoryResponseDtos.size());
                Assertions.assertEquals(user.getAddress(), categoryResponseDto.getAddress());
                Assertions.assertEquals(user.getUsername(), categoryResponseDto.getUsername());
                Assertions.assertEquals(post.getCategory(), categoryResponseDto.getCategoryName());
                Assertions.assertEquals(post.getTitle(), categoryResponseDto.getTitle());
            }

            @Test
            @DisplayName("검색조건1 : (cloth or food)  and (검색조건2 : 서울특별시)")
            void test2() {
                //given
                List<String> categoryName = new ArrayList<>();
                categoryName.add("cloth");
                categoryName.add("food");


                List<String> city = new ArrayList<>();
                city.add("서울특별시");


                CategoryDto.RequestDto categoryFilter = CategoryDto.RequestDto.builder()
                        .categoryName(categoryName)
                        .address(city)
                        .build();


                //when
                Pageable pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                List<CategoryResponseDto> categoryResponseDtos = categoryService.categoryFilter(categoryFilter, pageRequest);


                //then
                Assertions.assertEquals(3, categoryResponseDtos.size());
            }

            @Test
            @DisplayName("검색조건1 : (cloth or food)  and (검색조건2 : 서울특별시 강서구)")
            void test3() {
                //given
                List<String> categoryName = new ArrayList<>();
                categoryName.add("cloth");
                categoryName.add("food");


                List<String> city = new ArrayList<>();
                city.add("서울특별시");
                city.add("강서구");


                CategoryDto.RequestDto categoryFilter = CategoryDto.RequestDto.builder()
                        .categoryName(categoryName)
                        .address(city)
                        .build();


                //when
                Pageable pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                List<CategoryResponseDto> categoryResponseDtos = categoryService.categoryFilter(categoryFilter, pageRequest);


                //then
                Assertions.assertEquals(1, categoryResponseDtos.size());
                Assertions.assertEquals(post2.getContent(), categoryResponseDtos.get(0).getContent());
                Assertions.assertEquals(post2.getUser().getNickname(), categoryResponseDtos.get(0).getNickname());
            }
        }


    }
}