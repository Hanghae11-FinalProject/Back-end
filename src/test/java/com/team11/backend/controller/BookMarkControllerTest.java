package com.team11.backend.controller;

import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.service.BookMarkService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
class BookMarkControllerTest {

    @Autowired
    BookMarkService bookMarkService;

    @Autowired
    BookMarkRepository bookMarkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;


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
    @DisplayName("즐겨찾기 테스트")
    class LikeTest{

        @Nested
        @DisplayName("Success")
        class Success{

            @Test
            @Order(1)
            @DisplayName("즐겨찾기 등록 되면 true 반환")
            void test1(){
                boolean bookMarkStatus = bookMarkService.addBookMark(user, post2.getId());
                assertTrue(bookMarkStatus);

            }

            @Test
            @Order(2)
            @DisplayName("유저가 가지고있는 즐겨찾기 목록 조회")
            void test2(){
                List<BookMarkDto> myBookMark = bookMarkService.findMyBookMark(user);

                assertEquals(1 , myBookMark.size());
                assertEquals(post2.getTitle(), myBookMark.get(0).getTitle());
                assertEquals(post2.getId(), myBookMark.get(0).getPostId());
            }


            @Test
            @Order(3)
            @DisplayName("즐겨찾기 취소 => order(1)에서 등록한 좋아요 취소 검증")
            void test3(){
                IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
                    bookMarkService.cancelBookMark(user, post2.getId());
                });
                assertEquals("존재하지 않는 대상입니다.",illegalArgumentException.getMessage());
            }
        }


        @Nested
        @DisplayName("Fail")
        class Fail{

        }
    }
}