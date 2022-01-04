package com.team11.backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team11.backend.component.AwsS3UploadService;
import com.team11.backend.component.FileComponent;
import com.team11.backend.component.FileUploadService;
import com.team11.backend.dto.*;
import com.team11.backend.model.*;
import com.team11.backend.repository.*;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FileUploadService fileUploadService;
    private final AwsS3UploadService awsS3UploadService;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;



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
                .map(this::toBookmarkResponseDto)
                .collect(Collectors.toList());

        List<CommentDto.ResponseDto> responseDtos = convertNestedStructure(commentRepository.findCommentByPost(post));
        return PostDto.DetailResponseDto.builder()
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
                .categoryName(post.getCategory())
                .bookMarkCount(bookMarkRepository.countByPost(post).orElse(0))
                .commentCount(commentRepository.countByPost(post).orElse(0))
                .comments(responseDtos)
                .createdAt(TimeConversion.timeConversion(post.getCreateAt()))
                .build();
    }

    private BookMarkDto.DetailResponseDto toBookmarkResponseDto(BookMark bookMark) {

        return BookMarkDto.DetailResponseDto.builder()
                .userId(bookMark.getUser().getId())
                .build();
    }

    //수정
    @Transactional
    public void editPostService(List<MultipartFile> images, String jsonString, Long postId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PostDto.PutRequestDto requestDto = objectMapper.readValue(jsonString,PostDto.PutRequestDto.class);

        List<ImageDto> imageDtoList = new ArrayList<>();
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("해당되는 포스트가 존재하지 않습니다.")
        );
        List<Image> imageList = post.getImages();
        List<Image> removeList = new ArrayList<>();
        List<Tag> tagList = post.getTags();

        //지금 가지고있는 이미지 파일 S3에서 삭제 and 이미지 디비 삭제
        for (Image image : imageList){
            for(ImageUrlDto imageUrlDto:requestDto.getImageUrlDtos()){
                if(image.getImageUrl().equals(imageUrlDto.getImageUrl())){
                    awsS3UploadService.deleteFile(image.getImageName());
                    imageRepository.deleteById(image.getId());
                    removeList.add(image);
                }
            }
        }

        for(Image image : removeList){
            imageList.remove(image);
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
        tagList = new ArrayList<>();

        //String 형태의 jsonString을 Dto로 변환부분


        putDtoParser(tagList, imageList, imageDtoList, requestDto);


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

        for (TagDto.RequestDto tagRequestDto: requestDto.getTagRequestDtos()) {
            Tag tag = Tag.builder()
                    .tagName(tagRequestDto.getTagName())
                    .build();
            tagList.add(tag);
        }
    }

    private void putDtoParser(List<Tag> tagList, List<Image> imageList, List<ImageDto> imageDtoList, PostDto.PutRequestDto requestDto) {
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
            Integer bookmarkCnt = bookMarkRepository.countByPost(post).orElse(0);
            Integer commentCnt = commentRepository.countByPost(post).orElse(0);

            MyPostDto.ResponseDto responseDto = MyPostDto.ResponseDto.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(TimeConversion.timeConversion(post.getCreateAt()))
                    .address(user.getAddress())
                    .currentState(post.getCurrentState())
                    .bookmarkCnt(bookmarkCnt)
                    .commentCnt(commentCnt)
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


    private List<CommentDto.ResponseDto> convertNestedStructure(List<Comment> comments) { //조회시 계층형 구조 만들기
        List<CommentDto.ResponseDto> result = new ArrayList<>();
        Map<Long, CommentDto.ResponseDto> map = new HashMap<>();
        comments.forEach(c -> {
            CommentDto.ResponseDto dto = new CommentDto.ResponseDto(c.getId(), c.getContent(), c.getUser().getId(), c.getUser().getNickname(), TimeConversion.timeConversion(c.getCreateAt()));
            map.put(dto.getId(), dto);
            if (c.getParent() != null)
                 map.get(c.getParent().getId()).getChildren().add(dto);//양방향 연관관계를 사용해서 자식 코멘트에 댓글 등록
            else result.add(dto);
        });
        return result;
    }

    @Transactional
    public void editCurrentState(CurrentStateDto currentStateDto, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new IllegalArgumentException("no post")
        );

        post.updateCurrentState(currentStateDto.getCurrentState());
    }
}
