package com.team11.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team11.backend.dto.PostDto;
import com.team11.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public void createPost(@RequestParam(name = "image",required = false) List<MultipartFile> images, @RequestParam(name = "data") String jsonString) throws IOException {
        postService.createPostService(images,jsonString);
    }

    @PutMapping("/api/posts/{postId}")
    public void editPost(@RequestParam(name = "image",required = false) List<MultipartFile> images, @RequestParam(name = "data") String jsonString,@PathVariable Long postId) throws IOException {
        postService.editPostService(images,jsonString,postId);
    }
}
