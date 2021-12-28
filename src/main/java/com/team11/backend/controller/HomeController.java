package com.team11.backend.controller;

import com.team11.backend.dto.PostDto;
import com.team11.backend.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//import java.awt.print.Pageable;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/api/posts")
    public List<PostDto.ResponseDto> getPosts(@PageableDefault(size = 6, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return homeService.findPostList(pageable);
    }

}
