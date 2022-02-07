package com.team11.backend.service;

/*
import com.team11.backend.config.S3MockConfig;
import com.team11.backend.dto.CommentDto;
import com.team11.backend.model.*;
import com.team11.backend.model.Tag;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
@Import(S3MockConfig.class)
class CommentServiceTest {

    @Autowired
    S3Mock s3Mock;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentService commentService;
    User user;
    User user1;
    Post post;
    @BeforeEach
    public void setup() {
        //유저
        user = User.builder()
                .username("1234@naver.com")
                .nickname("woojin")
                .password("1234aabb@@")
                .address("서울특별시 강북구")
                .build();

        userRepository.save(user);

        user1 = User.builder()
                .username("wwww@naver.com")
                .nickname("aaaaa")
                .password("1234aabb@@")
                .address("서울특별시 강남구")
                .build();

        userRepository.save(user1);

        Image image = Image.builder()
                .imageUrl("sfsefsef")
                .imageName("sefsef")
                .build();
        List<Image> list = new ArrayList<>();
        list.add(image);

        com.team11.backend.model.Tag tag = com.team11.backend.model.Tag.builder()
                .tagName("김치")
                .build();
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);

        post = Post.builder()
                .tags(tagList)
                .images(list)
                .category("food")
                .user(user)
                .content("수박")
                .title("수박")
                .exchangeItem("난이거")
                .myItem("내아이템")
                .currentState(CurrentState.Proceeding)
                .build();

        postRepository.save(post);
    }

    @Nested
    @DisplayName("즐겨찾기 테스트")
    class CommentTest {

        @Nested
        @DisplayName("Success")
        class Success {

            @Test
            @DisplayName("첫 댓글 등록")
            void test1(){
                Long userId = user.getId();
                Long postId = post.getId();
                String content = "안녕하세요";

                CommentDto.RequestDto commentRequest = CommentDto.RequestDto.builder()
                        .postId(postId)
                        .content(content)
                        .build();

                CommentDto.ResponseDto responseDto = commentService.create(commentRequest, userId);

                assertEquals(commentRequest.getContent(),responseDto.getContent());
                assertEquals(1, post.getCommentCnt());
            }

     */
/*       @Test
            @DisplayName("댓글에 대댓글 등록")
            void test2(){

                Long userId = user.getId();
                Long postId = post.getId();
                String content = "안녕하세요";

                CommentDto.RequestDto commentRequest = CommentDto.RequestDto.builder()
                        .postId(postId)
                        .content(content)
                        .build();

                CommentDto.ResponseDto responseDto = commentService.create(commentRequest, userId);


                Long userId1 = user1.getId();
                Long postId1 = post.getId();
                String content1 = "안녕하세요1";

                CommentDto.RequestDto commentRequest1 = CommentDto.RequestDto.builder()
                        .postId(postId1)
                        .parentId(postId)
                        .content(content1)
                        .build();

                CommentDto.ResponseDto responseDto1 = commentService.create(commentRequest1, userId1);

                assertEquals(commentRequest1.getContent(),responseDto1.getContent());
                assertEquals(2, post.getCommentCnt());
            }*//*

        }

    }

    @AfterAll
    public void shutdownMockS3() {
        s3Mock.stop();
    }
}*/
