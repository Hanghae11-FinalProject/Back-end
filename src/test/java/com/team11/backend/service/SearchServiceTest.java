package com.team11.backend.service;

import com.team11.backend.dto.SearchDto;
import com.team11.backend.model.*;
import com.team11.backend.model.Tag;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.TagRepository;
import com.team11.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
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
    Post post1;
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
                .tagName("너구리")
                .build();
        tagRepository.save(lamen);

        List<Tag> tagList = new ArrayList<>();
        tagList.add(mando);
        tagList.add(lamen);

        post = Post.builder()
                .myItem("아이템")
                .exchangeItem("아이템")
                .currentState(CurrentState.Proceeding)
                .title("팝니다")
                .content("냉동만두랑 피자랑 바꾸실분")
                .user(user)
                .myItem("에어컨")
                .exchangeItem("난로")
                .category("food")
                .tags(tagList)
                .build();

        postRepository.save(post);

        Tag bulgogi = Tag.builder()
                .tagName("불고기피자")
                .build();
        tagRepository.save(bulgogi);

        Tag cheeze = Tag.builder()
                .tagName("치즈피자")
                .build();
        tagRepository.save(cheeze);
        Tag mandu = Tag.builder()
                .tagName("만두")
                .build();
        tagRepository.save(mandu);

        List<Tag> tagList1 = new ArrayList<>();
        tagList1.add(bulgogi);
        tagList1.add(cheeze);
        tagList1.add(mandu);

        post1 = Post.builder()
                .myItem("아이템")
                .exchangeItem("아이템")
                .currentState(CurrentState.Proceeding)
                .title("삽니다")
                .content("만두랑 떢볶이")
                .myItem("선풍기")
                .exchangeItem("리모컨")
                .user(user)
                .category("food")
                .tags(tagList1)
                .build();

        postRepository.save(post1);
    }


    @Nested
    @DisplayName("검색조건")
    class search {

        @Nested
        @DisplayName("성공 케이스")
        class success {

            @Test
            @DisplayName("태그로 너구리 검색 ")
            void test_1() {
                //given
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                SearchDto.RequestDto searchRequest = SearchDto.RequestDto
                        .builder()
                        .keyword(Collections.singletonList("너구리"))
                        .build();

                //when
                SearchDto.TotalResponseDto responseDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(1, responseDtos.getPosts().size());
                Assertions.assertEquals(post.getTitle(), responseDtos.getPosts().get(0).getTitle());
                Assertions.assertEquals(post.getContent(), responseDtos.getPosts().get(0).getContent());
            }

            @Test
            @DisplayName("만두 검색 : 예상 결과값 2개")
            void test_2(){

                //given
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                SearchDto.RequestDto searchRequest = SearchDto.RequestDto
                        .builder()
                        .keyword(Collections.singletonList("만두"))
                        .build();
                //when
                SearchDto.TotalResponseDto responseDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(2, responseDtos.getPosts().size());
                Assertions.assertEquals(post.getTitle(), responseDtos.getPosts().get(1).getTitle());
                Assertions.assertEquals(post.getContent(), responseDtos.getPosts().get(1).getContent());
                Assertions.assertEquals(post1.getTitle(), responseDtos.getPosts().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), responseDtos.getPosts().get(0).getContent());
            }

            @Test
            @DisplayName("myItem, exchangeItem 으로 조건 검색")
            void test_3(){

                //given
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                SearchDto.RequestDto searchRequest = SearchDto.RequestDto
                        .builder()
                        .keyword(Collections.singletonList("선풍기"))
                        .build();
                //when
                SearchDto.TotalResponseDto responseDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(1, responseDtos.getPosts().size());
                Assertions.assertEquals(post1.getTitle(), responseDtos.getPosts().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), responseDtos.getPosts().get(0).getContent());
            }

            @Test
            @DisplayName("content로 조건 검색")
            void test_4(){

                //given
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                SearchDto.RequestDto searchRequest = SearchDto.RequestDto
                        .builder()
                        .keyword(Collections.singletonList("만두"))
                        .build();
                //when
                SearchDto.TotalResponseDto responseDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(2, responseDtos.getPosts().size());
                Assertions.assertEquals(post1.getTitle(), responseDtos.getPosts().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), responseDtos.getPosts().get(0).getContent());
                Assertions.assertEquals(post.getTitle(), responseDtos.getPosts().get(1).getTitle());
                Assertions.assertEquals(post.getContent(), responseDtos.getPosts().get(1).getContent());
            }


            @Test
            @DisplayName("title 조건 검색")
            void test_5(){

                //given
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                SearchDto.RequestDto searchRequest = SearchDto.RequestDto
                        .builder()
                        .keyword(Collections.singletonList("팝니다"))
                        .build();
                //when
                SearchDto.TotalResponseDto responseDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(1, responseDtos.getPosts().size());
                Assertions.assertEquals(post.getTitle(), responseDtos.getPosts().get(0).getTitle());
                Assertions.assertEquals(post.getContent(), responseDtos.getPosts().get(0).getContent());
            }

        }


        @Nested
        @DisplayName("실패 케이스")
        class fail {

        }
    }
}