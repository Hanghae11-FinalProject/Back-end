package com.team11.backend.controller;

import com.team11.backend.component.FileUploadService;
import com.team11.backend.dto.MyPostDto;
import com.team11.backend.model.User;
import com.team11.backend.security.oauth2.service.CustomUserDetails;
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

    @PostMapping("/api/posts")
    public void createPost(
            @RequestParam(name = "image",required = false) List<MultipartFile> images,
            @RequestParam(name = "data") String jsonString,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        postService.createPostService(images,jsonString,userDetails);
    }


    @PutMapping("/api/posts/{postId}")
    public void editPost(@RequestParam(name = "image",required = false) List<MultipartFile> images, @RequestParam(name = "data") String jsonString,@PathVariable Long postId) throws IOException {
        postService.editPostService(images,jsonString,postId);
    }

    @GetMapping("/api/myposts")
    public List<MyPostDto.ResponseDto> showMyPost(@AuthenticationPrincipal CustomUserDetails userDetails){
        User user = userDetails.getUser();
        return postService.showMyPostService(user);
    }
}
