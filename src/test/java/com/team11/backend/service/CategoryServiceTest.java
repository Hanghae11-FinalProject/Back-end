package com.team11.backend.service;

import com.team11.backend.config.S3MockConfig;
import com.team11.backend.dto.CategoryDto;
import com.team11.backend.dto.querydto.CategoryQueryDto;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.repository.querydsl.CategoryRepository;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
@Import(S3MockConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceTest {

    @Autowired
    S3Mock s3Mock;

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
                .tags(new ArrayList<>())
                .images(new ArrayList<>())
                .category("cloth")
                .user(user)
                .content("나이키?")
                .title("나이키")
                .exchangeItem("난이거")
                .myItem("내아이템")
                .currentState(CurrentState.Proceeding)
                .build();

        postRepository.save(post);

        post1 = Post.builder()
                .tags(new ArrayList<>())
                .images(new ArrayList<>())
                .category("food")
                .user(user)
                .content("수박")
                .title("수박")
                .exchangeItem("난이거")
                .myItem("내아이템")
                .currentState(CurrentState.Proceeding)
                .build();

        postRepository.save(post1);


        post2 = Post.builder()
                .tags(new ArrayList<>())
                .images(new ArrayList<>())
                .category("food")
                .user(user1)
                .content("수박")
                .title("수박")
                .exchangeItem("난이거")
                .myItem("내아이템")
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
                List<CategoryQueryDto> categoryQueryDtos = categoryService.categoryFilter(categoryFilter, pageRequest);
                CategoryQueryDto categoryQueryDto = categoryQueryDtos.get(0);

                //then
                Assertions.assertEquals(1, categoryQueryDtos.size());
                Assertions.assertEquals(user.getAddress(), categoryQueryDto.getAddress());
                Assertions.assertEquals(user.getUsername(), categoryQueryDto.getUsername());
                Assertions.assertEquals(post.getCategory(), categoryQueryDto.getCategoryName());
                Assertions.assertEquals(post.getTitle(), categoryQueryDto.getTitle());
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
                List<CategoryQueryDto> categoryQueryDtos = categoryService.categoryFilter(categoryFilter, pageRequest);


                //then
                Assertions.assertEquals(3, categoryQueryDtos.size());
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
                List<CategoryQueryDto> categoryQueryDtos = categoryService.categoryFilter(categoryFilter, pageRequest);


                //then
                Assertions.assertEquals(1, categoryQueryDtos.size());
                Assertions.assertEquals(post2.getContent(), categoryQueryDtos.get(0).getContent());
                Assertions.assertEquals(post2.getUser().getNickname(), categoryQueryDtos.get(0).getNickname());
            }

            @Test
            @DisplayName("검색조건 : NULL or 빈칸 ,  기대값 : 전체조회")
            void test4(){
                List<String> categoryName = new ArrayList<>();
                List<String> city = new ArrayList<>();

                CategoryDto.RequestDto categoryFilter = CategoryDto.RequestDto.builder()
                        .categoryName(categoryName)
                        .address(city)
                        .build();

                //when
                Pageable pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                List<CategoryQueryDto> categoryQueryDtos = categoryService.categoryFilter(categoryFilter, pageRequest);


                //then
                Assertions.assertEquals(3, categoryQueryDtos.size());
            }
            
            @DisplayName("검색조건: NULL OR 빈칸 , 주소 : 강북구")
            @Test
            void test5(){
                //given
                List<String> categoryName = new ArrayList<>();
                List<String> city = new ArrayList<>();
                city.add("강북구");

                CategoryDto.RequestDto categoryFilter = CategoryDto.RequestDto.builder()
                        .categoryName(categoryName)
                        .address(city)
                        .build();

                //when
                Pageable pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                List<CategoryQueryDto> categoryQueryDtos = categoryService.categoryFilter(categoryFilter, pageRequest);


                //then
                Assertions.assertEquals(2, categoryQueryDtos.size());
            }
        }

    }

    @AfterAll
    public void shutdownMockS3(){
        s3Mock.stop();
    }
}
