package com.team11.backend.controller;

import com.team11.backend.dto.CommentDto;
import com.team11.backend.security.UserDetailsImpl;
import com.team11.backend.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/api/comments")
    public CommentDto.ResponseDto addComment(@Valid @RequestBody CommentDto.RequestDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return commentService.create(requestDto,userDetails.getUser().getId());
    }

    @DeleteMapping("/api/comments/{commentId}")
    public Long deleteComment(@PathVariable Long commentId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        return commentService.deleteComment(commentId,userDetails.getUser());
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Result<T> {
        T data;
    }
}
