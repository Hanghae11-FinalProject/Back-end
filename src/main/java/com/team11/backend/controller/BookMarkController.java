package com.team11.backend.controller;

import com.team11.backend.dto.CategoryDto;
import com.team11.backend.security.UserDetailsImpl;
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
    public ResponseEntity<?> addBookMark(@AuthenticationPrincipal UserDetailsImpl customUserDetails,
                                         @PathVariable Long postId
    ) {
        if(customUserDetails == null){
            throw new IllegalArgumentException("token not");
        }
        log.info("BookMarkAddPostUser={}", customUserDetails.getUser().getNickname());
        CategoryDto.ResponseDto result;
        result = bookMarkService.addBookMark(customUserDetails.getUser(), postId);

        return result != null? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.OK);
    }

    @DeleteMapping("/api/bookmark/{postId}")
    public CategoryDto.ResponseDto cancelBookMark(@AuthenticationPrincipal UserDetailsImpl customUserDetails,
                                                  @PathVariable Long postId
    ) {
        log.info("BookMarkCancelUser={}", customUserDetails.getUser().getNickname());
        return bookMarkService.cancelBookMark(customUserDetails.getUser(), postId);
    }

    @GetMapping("/api/bookmark")
    public Result<?> lookupBookMark(@AuthenticationPrincipal UserDetailsImpl customUserDetails
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
