package com.team11.backend.controller;

import com.team11.backend.dto.CurrentStateDto;
import com.team11.backend.dto.MyPostResponseDto;
import com.team11.backend.dto.querydto.PostDetailQueryDto;
import com.team11.backend.model.User;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    //게시물 등록
    @PostMapping("/api/posts")
    public void createPost(
            @RequestParam(name = "image",required = false) List<MultipartFile> images,
            @RequestParam(name = "data") String jsonString,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        postService.createPostService(images,jsonString,userDetails);
    }

    // 게시글 상세조회
    @GetMapping("/api/posts/{postId}")
    public PostDetailQueryDto getDetail(@PathVariable Long postId ) {
        return postService.getDetail(postId);
    }

    @PutMapping("/api/posts/{postId}")
    public void editPost(@RequestParam(name = "image",required = false) List<MultipartFile> images, @RequestParam(name = "data") String jsonString,@PathVariable Long postId) throws IOException {
        postService.editPostService(images,jsonString,postId);
    }

    //게시물 삭제
    @DeleteMapping("/api/posts/{postId}")
    public void boardDelete(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long postId
    ) {
        postService.deletePost(userDetails.getUser(), postId);
    }

    //게시물 수정
    @PutMapping("/api/currentstate/{postId}")
    public void updateCurrentState(@RequestBody CurrentStateDto currentStateDto ,@PathVariable Long postId){
        postService.editCurrentState(currentStateDto,postId);
    }

    //내가 등록한 게시물 조회
    @GetMapping("/api/myposts")
    public List<MyPostResponseDto> showMyPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return postService.showMyPostService(user);
    }
}
