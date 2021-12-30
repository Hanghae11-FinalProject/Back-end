package com.team11.backend.service;

import com.team11.backend.config.S3MockConfig;
import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.BookMarkRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
@Import(S3MockConfig.class)
class BookMarkServiceTest {

    @Autowired
    S3Mock s3Mock;

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
    Post post1;
    Post post2;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .username("1234@naver.com")
                .nickname("woojin")
                .password("1234aabb@@")
                .address("서울특별시 강북구")
                .build();

        userRepository.save(user);

        user1 = User.builder()
                .username("asdf@facebook.com")
                .nickname("woojinn")
                .password("1234aabb@@")
                .address("서울특별시 강서구")
                .build();

        userRepository.save(user1);

        post1 = Post.builder()
                .category("food")
                .user(user)
                .content("수박")
                .title("수박")
                .exchangeItem("난이거")
                .myItem("내아이템")
                .currentState(CurrentState.Proceeding)
                .exchangeItem("exchangeItem")
                .myItem("myItem")
                .bookMarks(new ArrayList<BookMark>(Arrays.asList(new BookMark[3])))
                .build();

        postRepository.save(post1);


        post2 = Post.builder()
                .category("cloth")
                .user(user1)
                .content("ddda")
                .title("aas")
                .exchangeItem("난이거")
                .myItem("내아이템")
                .currentState(CurrentState.Proceeding)
                .exchangeItem("exchangeItem")
                .myItem("myItem")
                .bookMarks(new ArrayList<BookMark>(Arrays.asList(new BookMark[3])))
                .build();

        postRepository.save(post2);

    }

    @Nested
    @DisplayName("즐겨찾기 테스트")
    class LikeTest {

        @Nested
        @DisplayName("Success")
        class Success {

            @Test
            @Order(1)
            @DisplayName("즐겨찾기 등록 되면 true 반환")
            void test1() {
                boolean b = bookMarkService.addBookMark(user, post2.getId());
                assertTrue(b);

            }

            @Test
            @Order(2)
            @DisplayName("유저가 가지고있는 즐겨찾기 목록 조회")
            void test2() {
                bookMarkService.addBookMark(user, post2.getId());
                List<BookMarkDto.ResponseDto> myBookMark = bookMarkService.findMyBookMark(user);


                assertEquals(1, myBookMark.size());
                assertEquals(post2.getTitle(), myBookMark.get(0).getTitle());
                assertEquals(post2.getId(), myBookMark.get(0).getPostId());
            }


            @Test
            @Order(3)
            @DisplayName("즐겨찾기 취소 => order(1)에서 등록한 좋아요 취소 검증")
            void test3() {
                IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
                    bookMarkService.cancelBookMark(user, post2.getId());
                });
                assertEquals("존재하지 않는 대상입니다.", illegalArgumentException.getMessage());
            }
        }


        @Nested
        @DisplayName("Fail")
        class Fail {

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
    public void shutdownMockS3() {
        s3Mock.stop();
    }
}