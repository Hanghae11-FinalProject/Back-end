package com.team11.backend.service;
/*

import com.team11.backend.config.S3MockConfig;
import com.team11.backend.dto.SearchDto;
import com.team11.backend.dto.querydto.SearchQueryDto;
import com.team11.backend.model.*;
import com.team11.backend.model.Tag;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.TagRepository;
import com.team11.backend.repository.UserRepository;
import io.findify.s3mock.S3Mock;
import io.micrometer.core.instrument.search.Search;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
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
@Import(S3MockConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchServiceTest {

    @Autowired
    S3Mock s3Mock;

    @Autowired
    SearchService searchService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagRepository tagRepository;

   */
/* @Autowired
    SearchRepositoryInterface searchRepositoryInterface;
*//*

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
                .images(new ArrayList<>())
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
                .images(new ArrayList<>())
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

*/
/*        //hot 인기검색 순위 더미데이터
        for (int i = 0; i < 10; i++) {
            for (int j = 9 - i; j > 0; j--) {
                searchRepositoryInterface.save(Search.builder()
                        .keyword("더미데이터" + j)
                        .build());
            }
        }*//*

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
                Page<SearchQueryDto> searchKeyword = searchService.keywordSearch(searchRequest, pageRequest);

                //then

                Assertions.assertEquals(1, searchKeyword.getNumberOfElements());
                Assertions.assertEquals(post.getTitle(),searchKeyword.getContent().get(0).getTitle());
                Assertions.assertEquals(post.getContent(), searchKeyword.getContent().get(0).getContent());
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
                Page<SearchQueryDto> searchKeyword = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(2, searchKeyword.getNumberOfElements());
                Assertions.assertEquals(post.getTitle(), searchKeyword.getContent().get(1).getTitle());
                Assertions.assertEquals(post.getContent(), searchKeyword.getContent().get(1).getContent());
                Assertions.assertEquals(post1.getTitle(), searchKeyword.getContent().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), searchKeyword.getContent().get(0).getContent());
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
                Page<SearchQueryDto> searchKeyword =searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(1, searchKeyword.getTotalElements());
                Assertions.assertEquals(post1.getTitle(), searchKeyword.getContent().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), searchKeyword.getContent().get(0).getContent());
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
                Page<SearchQueryDto> searchKeyword =searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(2, searchKeyword.getNumberOfElements());
                Assertions.assertEquals(post1.getTitle(), searchKeyword.getContent().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), searchKeyword.getContent().get(0).getContent());
                Assertions.assertEquals(post.getTitle(), searchKeyword.getContent().get(1).getTitle());
                Assertions.assertEquals(post.getContent(), searchKeyword.getContent().get(1).getContent());
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
                Page<SearchQueryDto> searchQueryDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(1, searchQueryDtos.getNumberOfElements());
                Assertions.assertEquals(post.getTitle(), searchQueryDtos.getContent().get(0).getTitle());
                Assertions.assertEquals(post.getContent(), searchQueryDtos.getContent().get(0).getContent());
            }

            @Test
            @DisplayName("카테고리 이름으로 검색")
            void test_6() {
                //given
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                SearchDto.RequestDto searchRequest = SearchDto.RequestDto
                        .builder()
                        .keyword(Collections.singletonList("food"))
                        .build();
                //when
                Page<SearchQueryDto> searchQueryDtos = searchService.keywordSearch(searchRequest, pageRequest);

                //then
                Assertions.assertEquals(2, searchQueryDtos.getNumberOfElements());
                Assertions.assertEquals(post.getTitle(), searchQueryDtos.getContent().get(1).getTitle());
                Assertions.assertEquals(post.getContent(), searchQueryDtos.getContent().get(1).getContent());
                Assertions.assertEquals(post1.getTitle(), searchQueryDtos.getContent().get(0).getTitle());
                Assertions.assertEquals(post1.getContent(), searchQueryDtos.getContent().get(0).getContent());

            }

     */
/*       @Test
            @DisplayName("Hot인기 검색어 순위 List index 0이 가장 검색많이된 값 그다음이 1 2 3 ")
            void test_6() {
                List<String> keywordRank = searchRepositoryInterface.findKeywordRank();
                assertEquals("더미데이터1",keywordRank.get(0));
                assertEquals("더미데이터2", keywordRank.get(1));
            }*//*

        }


        @Nested
        @DisplayName("실패 케이스")
        class fail {

        }
    }

    @AfterAll
    public void shutdownMockS3(){
        s3Mock.stop();
    }
}*/
