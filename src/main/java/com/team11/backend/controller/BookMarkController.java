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

    //북마크 등록
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

        //북마크가 안눌린상태에서 처음 등록하면 값을 Response에 반환, 이미 눌린상태라면 false 반환
        return result != null? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.OK);     }

    //북마크 등록 취소
    @DeleteMapping("/api/bookmark/{postId}")
    public CategoryDto.ResponseDto cancelBookMark(@AuthenticationPrincipal UserDetailsImpl customUserDetails,
                                                  @PathVariable Long postId
    ) {
        log.info("BookMarkCancelUser={}", customUserDetails.getUser().getNickname());
        return bookMarkService.cancelBookMark(customUserDetails.getUser(), postId);
    }

    //내가 등록한 북마크 전체 조회
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
