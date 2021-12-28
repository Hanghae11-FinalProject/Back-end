package com.team11.backend.service;

import com.team11.backend.dto.BookMarkDto;
import com.team11.backend.dto.PostDto;
import com.team11.backend.model.BookMark;
import com.team11.backend.model.Post;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.team11.backend.model.QPost.post;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final PostRepository postRepository;

    //메인화면 게시글 리스트
    @Transactional
    public List<PostDto.ResponseDto> findPostList(Pageable pageable) {
        List<PostDto.ResponseDto> postResponseDtos = new ArrayList<>();
        PageImpl<Post> postList = postRepository.findAllByOrderByCreateAtDesc(pageable);

        for(Post post : postList) {

            List<BookMarkDto.DetailResponseDto> bookMarkResponseDtoList = post.getBookMarks().stream()
                    .map(e -> toBookmarkResponseDto(e))
                    .collect(Collectors.toList());

            PostDto.ResponseDto postResponseDto = PostDto.ResponseDto.builder()
                    .postId(post.getId())
                    .nickname(post.getUser().getNickname())
                    .bookMarks(bookMarkResponseDtoList)
                    .title(post.getTitle())
                    .content(post.getContent())
                    .address(post.getUser().getAddress())
                    .tags(post.getTags())
                    .images(post.getImages())
                    .myItem(post.getMyItem())
                    .exchangeItem(post.getExchangeItem())
                    .currentState(post.getCurrentState())
                    .createdAt(TimeConversion.timeConversion(post.getCreateAt()))
                    .build();

            postResponseDtos.add(postResponseDto);
        }

        return postResponseDtos;
    }

    private BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        System.out.println(bookMark.getUser().getUsername());
        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
    }
}
