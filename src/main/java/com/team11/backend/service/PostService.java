package com.team11.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.component.FileComponent;
import com.team11.backend.dto.PostDto;
import com.team11.backend.model.Category;
import com.team11.backend.model.Image;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final FileComponent fileComponent;
    private final PostRepository postRepository;

    public void createPostService(List<MultipartFile> images, String jsonString) throws IOException {
        //파일 업로드를 성공하고 이미지 리스트를 반환하는 함수.
        List<Image> imageList = fileComponent.fileUploadAndGetImageList(images);

        //String 형태의 jsonString을 Dto로 변환부분
        ObjectMapper objectMapper = new ObjectMapper();
        PostDto.RequestDto requestDto = objectMapper.readValue(jsonString,PostDto.RequestDto.class);

        //아직 회원가입 로그인 기능을 만들지 않았기때문에 유저를 만들어준다.

        User user = User.builder()
                .address("asdasd")
                .nickname("asdasdasd")
                .password("asdasdasd")
                .username("asdasdasd")
                .profileImg("asdasdasd")
                .build();
        ///////////////////////////////////////////////////////

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .currentState(requestDto.getCurrentState())
                .images(imageList)
                .user(user)
                .build();

        //Post저장
        postRepository.save(post);
    }
}
