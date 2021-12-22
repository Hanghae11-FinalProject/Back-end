package com.team11.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.component.AwsS3UploadService;
import com.team11.backend.component.FileComponent;
import com.team11.backend.component.FileUploadService;
import com.team11.backend.dto.ImageDto;
import com.team11.backend.dto.TagDto;
import com.team11.backend.model.Tag;
import com.team11.backend.model.User;
import com.team11.backend.dto.PostDto;
import com.team11.backend.model.Image;
import com.team11.backend.model.Post;

import com.team11.backend.repository.ImageRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final FileComponent fileComponent;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FileUploadService fileUploadService;
    private final AwsS3UploadService awsS3UploadService;
    private final TagRepository tagRepository;

    @Transactional
    public void createPostService(List<MultipartFile> images, String jsonString) throws IOException {
        List<Tag> tagList = new ArrayList<>();
        List<Image> imageList = new ArrayList<>();
        //파일 업로드를 성공하고 이미지 리스트를 반환하는 함수.
//        List<ImageDto> imageDtoList = fileComponent.fileUploadAndGetImageList(images);
        List<ImageDto> imageDtoList = new ArrayList<>();

        for (MultipartFile image: images){
            ImageDto imageDto = fileUploadService.uploadImage(image);
            imageDtoList.add(imageDto);
        }
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
        dtoParser(tagList, imageList, imageDtoList, requestDto);

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .currentState(requestDto.getCurrentState())
                .images(imageList)
                .user(user)
                .tags(tagList)
                .build();

        //Post저장
        postRepository.save(post);
    }

    //수정
    @Transactional
    public void editPostService(List<MultipartFile> images, String jsonString, Long postId) throws IOException {

        List<ImageDto> imageDtoList = new ArrayList<>();
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당되는 포스트가 존재하지 않습니다.")
        );
        List<Image> imageList = post.getImages();
        List<Tag> tagList = post.getTags();

        //지금 가지고있는 이미지 파일 S3에서 삭제 and 이미지 디비 삭제
        for (Image image : imageList){
            awsS3UploadService.deleteFile(image.getImageName());
            imageRepository.deleteById(image.getId());
        }
        //지금 가지고있는 테그 삭제
        for (Tag tag : tagList){
            tagRepository.deleteById(tag.getId());
        }
        //바꿀 이미지 S3에 저장
        for (MultipartFile image: images){
            ImageDto imageDto = fileUploadService.uploadImage(image);
            imageDtoList.add(imageDto);
        }

        // 이미지 리스트, 테그 리스트 다시 초기화
        imageList = new ArrayList<>();
        tagList = new ArrayList<>();

        //String 형태의 jsonString을 Dto로 변환부분
        ObjectMapper objectMapper = new ObjectMapper();
        PostDto.RequestDto requestDto = objectMapper.readValue(jsonString,PostDto.RequestDto.class);

        dtoParser(tagList, imageList, imageDtoList, requestDto);


        post.updatePost(requestDto,imageList,tagList);

    }

    private void dtoParser(List<Tag> tagList, List<Image> imageList, List<ImageDto> imageDtoList, PostDto.RequestDto requestDto) {
        for (ImageDto imagedto:imageDtoList) {
            Image image = Image.builder()
                    .imageName(imagedto.getImageName())
                    .imageUrl(imagedto.getImageUrl())
                    .build();
            imageList.add(image);
        }

        for (TagDto.RequestDto tagRequestDto: requestDto.getTagRequsetDtos()) {
            Tag tag = Tag.builder()
                    .tagName(tagRequestDto.getTagName())
                    .build();
            tagList.add(tag);
        }
    }
}
