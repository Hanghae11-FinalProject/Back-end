package com.team11.backend.service;

import com.team11.backend.dto.CommentDto;
import com.team11.backend.model.Comment;
import com.team11.backend.model.User;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
import com.team11.backend.timeConversion.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto.ResponseDto create(CommentDto.RequestDto requestDto, Long userId) {

        return convertCommentToDto(commentRepository.save(
                Comment.createComment(
                        requestDto.getContent(),
                        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보 입니다")),
                        postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")),
                        Optional.ofNullable(requestDto.getParentId())  //부모 코멘트가 존재하는지 확인
                                .map(id -> commentRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                                .orElse(null))));
    }

    //댓글 삭제
    @Transactional
    public Long deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("본인 댓글만 삭제 할 수있습니다.");
        commentRepository.delete(comment);
        comment.getPost().minusCommentCount();
        return commentId;
    }

    public static CommentDto.ResponseDto convertCommentToDto(Comment comment) {
        return new CommentDto.ResponseDto(comment.getId(), comment.getContent(), comment.getUser().getId(), comment.getUser().getNickname(), comment.getUser().getProfileImg(),TimeConversion.timeConversion(comment.getCreatedAt()));
    }
}
