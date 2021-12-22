package com.team11.backend.controller;

import com.team11.backend.security.oauth2.service.CustomUserDetails;
import com.team11.backend.service.BookMarkService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/api/bookmark/{postId}")
    public ResponseEntity<?> addBookMark(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @PathVariable Long postId
    ) {
        log.info("BookMarkAddPostUser={}", customUserDetails.getUser().getNickname());
        boolean result = false;
        result = bookMarkService.addBookMark(customUserDetails.getUser(), postId);

        return result ?
                new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.OK);
    }

    @DeleteMapping("/api/bookmark/{postId}")
    public Long cancelBookMark(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @PathVariable Long postId
    ) {
        log.info("BookMarkCancelUser={}", customUserDetails.getUser().getNickname());
        return bookMarkService.cancelBookMark(customUserDetails.getUser(), postId);
    }

    @GetMapping("/api/bookmark")
    public Result<?> lookupBookMark(@AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        log.info("MyBookMarkAllLookUp={}",customUserDetails.getUser().getNickname());
        return new Result<>(bookMarkService.findMyBookMark(customUserDetails.getUser()));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Result<T> {
        T data;
    }
}