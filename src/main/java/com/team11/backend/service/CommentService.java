package com.team11.backend.service;

import com.team11.backend.dto.CommentDto;
import com.team11.backend.model.Comment;
import com.team11.backend.model.Post;
import com.team11.backend.model.User;
import com.team11.backend.repository.CommentRepository;
import com.team11.backend.repository.PostRepository;
import com.team11.backend.repository.UserRepository;
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

    public List<CommentDto.ResponseDto> readAll(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return convertNestedStructure(commentRepository.findCommentByPost(post));
    }


    @Transactional
    public CommentDto.ResponseDto create(CommentDto.RequestDto requestDto, Long userId) {
        return convertCommentToDto(commentRepository.save(
                new Comment(
                        requestDto.getContent(),
                        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보 입니다")),
                        postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")),
                        Optional.ofNullable(requestDto.getParentId())  //부모 코멘트가 존재하는지 확인
                                .map(id -> commentRepository.findById(id).orElseThrow(IllegalArgumentException::new)) //만약 id
                                .orElse(null))));
    }

    @Transactional
    public Long deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("본인 댓글만 삭제 할 수있습니다.");
        commentRepository.delete(comment);

        return commentId;
    }

    public static CommentDto.ResponseDto convertCommentToDto(Comment comment){ //댓글삭제
        return new CommentDto.ResponseDto(comment.getId(),comment.getContent(),comment.getUser().getId(), comment.getUser().getNickname());
    }


    private List<CommentDto.ResponseDto> convertNestedStructure(List<Comment> comments) { //계층형 구조 만들기
        List<CommentDto.ResponseDto> result = new ArrayList<>();
        Map<Long, CommentDto.ResponseDto> map = new HashMap<>();
        comments.forEach(c -> {
            CommentDto.ResponseDto dto = convertCommentToDto(c);
            map.put(dto.getId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);//양방향 연관관계를 사용해서 자식 코멘트에 댓글 등록
            result.add(dto);
        });
        return result;
    }

}
