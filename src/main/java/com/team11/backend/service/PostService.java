package com.team11.backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.component.AwsS3UploadService;
import com.team11.backend.component.FileComponent;
import com.team11.backend.component.FileUploadService;
import com.team11.backend.dto.*;
import com.team11.backend.model.*;
import com.team11.backend.repository.ImageRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.TagRepository;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void createPostService(List<MultipartFile> images, String jsonString, UserDetailsImpl userDetails) throws IOException {
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



        User user = userDetails.getUser();
        dtoParser(tagList, imageList, imageDtoList, requestDto);

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .currentState(requestDto.getCurrentState())
                .myItem(requestDto.getMyItem())
                .exchangeItem(requestDto.getExchangeItem())
                .images(imageList)
                .user(user)
                .tags(tagList)
                .build();

        //Post저장
        postRepository.save(post);
    }

    // 상세페이지
    @Transactional
    public PostDto.DetailResponseDto getDetail(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 포스트가 존재하지 않습니다."));

        List<BookMarkDto.DetailResponseDto> bookMarkResponseDtoList = post.getBookMarks().stream()
                .map(e -> toBookmarkResponseDto(e))
                .collect(Collectors.toList());

        PostDto.DetailResponseDto postResponseDto = PostDto.DetailResponseDto.builder()
                .postId(postId)
                .nickname(post.getUser().getNickname())
                .profileImg(post.getUser().getProfileImg())
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

        return postResponseDto;
    }

    private BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        System.out.println(bookMark.getUser().getUsername());
        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
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

    public List<MyPostDto.ResponseDto> showMyPostService(User user) {
        List<Post> postList = postRepository.findAllByUser(user);
        List<MyPostDto.ResponseDto> responseDtos = new ArrayList<>();
        for (Post post : postList){
            MyPostDto.ResponseDto responseDto = MyPostDto.ResponseDto.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .currentState(post.getCurrentState())
                    .build();

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    //삭제
    @Transactional
    public void deletePost(User user, Long postId) {

        // 포스트 조회
        Post findPost = postRepository.findById(postId).orElseThrow(
                () ->  new IllegalArgumentException("해당되는 포스트가 존재하지 않습니다.")
        );

        // 사용자 조회 (작성자와 같은지 확인)
        if (!findPost.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당되는 사용자가 아닙니다");
        }

        // post 삭제
        postRepository.deleteById(postId);
    }
}
