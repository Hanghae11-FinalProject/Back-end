package com.team11.backend.service;

import com.team11.backend.dto.PostDto;
import com.team11.backend.model.CurrentState;
import com.team11.backend.model.Image;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final PostRepository postRepository;

    //메인화면 게시글 리스트
    @Transactional
    public List<PostDto.ResponseDto> findPostList() {
        List<PostDto.ResponseDto> postResponseDtos = new ArrayList<>();
        List<Post> postList = postRepository.findAllByOrderByCreateAtDesc();
        for(Post post : postList) {

            PostDto.ResponseDto postResponseDto = PostDto.ResponseDto.builder()
                    .postId(post.getId())
                    .nickname(post.getUser().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .address(post.getUser().getAddress())
                    .images(post.getImages())
                    .currentState(post.getCurrentState())
                    .createdAt(TimeConversion.timeConversion(post.getCreateAt()))
                    .build();

            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }
}
