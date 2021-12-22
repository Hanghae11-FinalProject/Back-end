package com.team11.backend.controller;

import com.team11.backend.dto.PostDto;
import com.team11.backend.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/api/posts")
    public List<PostDto.ResponseDto> getPosts() {
        return homeService.findPostList();
    }

}